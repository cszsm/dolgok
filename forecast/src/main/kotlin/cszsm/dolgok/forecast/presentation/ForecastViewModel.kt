package cszsm.dolgok.forecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cszsm.dolgok.core.domain.models.FetchedData
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import cszsm.dolgok.forecast.domain.usecases.FetchDailyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.FetchFirstDayHourlyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.FetchMoreHourlyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.IsMoreHourlyForecastAllowedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

internal class ForecastViewModel(
    private val forecastRepository: ForecastRepository,
    private val fetchFirstDayHourlyForecastUseCase: FetchFirstDayHourlyForecastUseCase,
    private val fetchMoreHourlyForecastUseCase: FetchMoreHourlyForecastUseCase,
    private val fetchDailyForecastUseCase: FetchDailyForecastUseCase,
    private val isMoreHourlyForecastAllowedUseCase: IsMoreHourlyForecastAllowedUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ForecastScreenState())
    val state: StateFlow<ForecastScreenState> = _state.asStateFlow()

    init {
        collectForecasts()
    }

    fun onEvent(event: ForecastScreenEvent) {
        when (event) {
            is ForecastScreenEvent.TimeResolutionChange -> onTimeResolutionChange(timeResolution = event.timeResolution)
            is ForecastScreenEvent.WeatherVariableChange -> onWeatherVariableChange(
                weatherVariable = event.weatherVariable
            )

            ForecastScreenEvent.HourlyForecastEndReach -> fetchMoreHourlyForecast()
        }
    }

    private fun collectForecasts() {
        viewModelScope.launch {
            forecastRepository.hourlyForecastStream.collect { hourlyForecast ->
                val moreAllowed = hourlyForecast.lastLoadedForecastDateTime?.let {
                    isMoreHourlyForecastAllowedUseCase(it)
                } ?: true
                _state.update {
                    it.copy(
                        hourlyForecastSectionState = it.hourlyForecastSectionState.copy(
                            forecast = hourlyForecast,
                            moreAllowed = moreAllowed,
                        )
                    )
                }
            }
        }

        viewModelScope.launch {
            forecastRepository.dailyForecastStream.collect { dailyForecast ->
                _state.update {
                    it.copy(
                        dailyForecastSectionState = it.dailyForecastSectionState.copy(
                            forecast = dailyForecast
                        )
                    )
                }
            }
        }
    }

    private fun onTimeResolutionChange(
        timeResolution: TimeResolution,
    ) {
        _state.update { state ->
            state.copy(
                selectedTimeResolution = timeResolution,
                selectedWeatherVariable =
                    if (state.selectedWeatherVariable in timeResolution.weatherVariables) {
                        state.selectedWeatherVariable
                    } else {
                        timeResolution.weatherVariables.first()
                    },
            )
        }
        when (timeResolution) {
            TimeResolution.HOURLY ->
                if (_state.value.hourlyForecastSectionState.forecast.data == null) fetchFirstDayHourlyForecast()

            TimeResolution.DAILY ->
                if (_state.value.dailyForecastSectionState.forecast.data == null) fetchDailyForecast()
        }
    }

    private fun onWeatherVariableChange(
        weatherVariable: WeatherVariable,
    ) {
        _state.update { state -> state.copy(selectedWeatherVariable = weatherVariable) }
    }

    private fun fetchFirstDayHourlyForecast() {
        viewModelScope.launch {
            fetchFirstDayHourlyForecastUseCase(
                latitude = LATITUDE,
                longitude = LONGITUDE,
            )
        }
    }

    private fun fetchMoreHourlyForecast() {
        if (!state.value.hourlyForecastSectionState.moreAllowed) return
        val lastLoadedDateTime =
            state.value.hourlyForecastSectionState.forecast.lastLoadedForecastDateTime ?: return

        viewModelScope.launch {
            fetchMoreHourlyForecastUseCase(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                lastLoadedDateTime = lastLoadedDateTime,
            )
        }
    }

    private fun fetchDailyForecast() {
        viewModelScope.launch {
            fetchDailyForecastUseCase(
                latitude = LATITUDE,
                longitude = LONGITUDE,
            )
        }
    }

    private val FetchedData<HourlyForecast>.lastLoadedForecastDateTime: LocalDateTime?
        get() = data?.hours?.keys?.lastOrNull()

    private companion object {
        const val LATITUDE: Float = 47.50f
        const val LONGITUDE: Float = 19.04f
    }
}