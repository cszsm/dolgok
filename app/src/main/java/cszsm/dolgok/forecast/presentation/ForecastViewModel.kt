package cszsm.dolgok.forecast.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cszsm.dolgok.forecast.domain.models.ForecastDay
import cszsm.dolgok.forecast.domain.usecases.GetDailyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.GetHourlyForecastUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val getDailyForecastUseCase: GetDailyForecastUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ForecastState())
    val state: StateFlow<ForecastState> = _state.asStateFlow()

    fun onLocationPermissionResult(granted: Boolean) {
        Log.d("dolgok", "location granted: $granted")
    }

    fun onTimeResolutionChange(
        selectedTimeResolution: TimeResolution,
    ) {
        _state.update { state ->
            state.copy(
                selectedTimeResolution = selectedTimeResolution,
                selectedWeatherVariable =
                    if (state.selectedWeatherVariable in selectedTimeResolution.weatherVariables) {
                        state.selectedWeatherVariable
                    } else {
                        selectedTimeResolution.weatherVariables.first()
                    },
            )
        }
        when (selectedTimeResolution) {
            TimeResolution.HOURLY ->
                if (_state.value.hourlyForecastByDay.isEmpty()) loadHourlyForecast(forecastDay = ForecastDay.TODAY)

            TimeResolution.DAILY ->
                if (_state.value.dailyForecast == null) loadDailyForecast()
        }
    }

    fun onWeatherVariableChange(
        selectedWeatherVariable: WeatherVariable,
    ) {
        _state.update { state -> state.copy(selectedWeatherVariable = selectedWeatherVariable) }
    }

    fun loadHourlyForecastForTheNextDay() {
        val lastLoadedDay = state.value.hourlyForecastByDay.entries.lastOrNull() ?: return
        if (!lastLoadedDay.value.successful) return
        val nextDay = lastLoadedDay.key.nextDay ?: return
        loadHourlyForecast(forecastDay = nextDay)
    }

    private fun loadHourlyForecast(
        forecastDay: ForecastDay,
    ) {
        if (state.value.hourlyForecastLoading) return

        _state.update { state -> state.copy(hourlyForecastLoading = true) }
        viewModelScope.launch {
            val hourlyForecast = getHourlyForecastUseCase(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                forecastDay = forecastDay,
            )
            _state.update { state ->
                val updatedForecast = state.hourlyForecastByDay.toMutableMap()
                updatedForecast[forecastDay] = hourlyForecast

                state.copy(
                    hourlyForecastByDay = updatedForecast,
                    hourlyForecastLoading = false,
                )
            }
        }
    }

    private fun loadDailyForecast() {
        if (state.value.dailyForecastLoading) return

        _state.update { state -> state.copy(dailyForecastLoading = true) }
        viewModelScope.launch {
            val dailyForecast = getDailyForecastUseCase(
                latitude = LATITUDE,
                longitude = LONGITUDE,
            )
            _state.update { state ->
                state.copy(
                    dailyForecast = dailyForecast,
                    dailyForecastLoading = false,
                )
            }
        }
    }

    private companion object {
        const val LATITUDE: Float = 47.50f
        const val LONGITUDE: Float = 19.04f
    }
}