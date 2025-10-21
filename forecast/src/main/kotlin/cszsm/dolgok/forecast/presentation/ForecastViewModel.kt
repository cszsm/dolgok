package cszsm.dolgok.forecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import cszsm.dolgok.forecast.domain.usecases.FetchDailyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.FetchFirstDayHourlyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.FetchMoreHourlyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.IsMoreForecastAllowedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ForecastViewModel(
    private val forecastRepository: ForecastRepository,
    private val fetchFirstDayHourlyForecastUseCase: FetchFirstDayHourlyForecastUseCase,
    private val fetchMoreHourlyForecastUseCase: FetchMoreHourlyForecastUseCase,
    private val fetchDailyForecastUseCase: FetchDailyForecastUseCase,
    private val isMoreForecastAllowedUseCase: IsMoreForecastAllowedUseCase,
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
                _state.update { it.copy(hourlyForecast = hourlyForecast) }
            }
        }

        viewModelScope.launch {
            forecastRepository.dailyForecastStream.collect { dailyForecast ->
                _state.update { it.copy(dailyForecast = dailyForecast) }
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
                if (_state.value.hourlyForecast.data == null) fetchFirstDayHourlyForecast()

            TimeResolution.DAILY ->
                if (_state.value.dailyForecast.data == null) fetchDailyForecast()
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
        val lastLoadedDateTime =
            state.value.hourlyForecast.data?.hours?.keys?.lastOrNull() ?: return
        if (!isMoreForecastAllowedUseCase(lastLoadedForecastDateTime = lastLoadedDateTime)) return

        viewModelScope.launch {
            fetchMoreHourlyForecastUseCase(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                lastLoadedDateTime = lastLoadedDateTime,
            )
        }
    }

    private fun fetchDailyForecast() {
        _state.update {
            it.copy(dailyForecast = it.dailyForecast.copy(loading = true))
        }
        viewModelScope.launch {
            fetchDailyForecastUseCase(
                latitude = LATITUDE,
                longitude = LONGITUDE,
            )
        }
    }

    private companion object {
        const val LATITUDE: Float = 47.50f
        const val LONGITUDE: Float = 19.04f
    }
}