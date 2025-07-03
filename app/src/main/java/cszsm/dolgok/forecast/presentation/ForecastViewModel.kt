package cszsm.dolgok.forecast.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun init() {
        loadHourlyForecast()
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
            TimeResolution.HOURLY -> if (_state.value.hourlyForecast == null) loadHourlyForecast()
            TimeResolution.DAILY -> if (_state.value.dailyForecast == null) loadDailyForecast()
        }
    }

    fun onWeatherVariableChange(
        selectedWeatherVariable: WeatherVariable,
    ) {
        _state.update { state -> state.copy(selectedWeatherVariable = selectedWeatherVariable) }
    }

    private fun loadHourlyForecast() {
        viewModelScope.launch {
            try {
                val hourlyForecast = getHourlyForecastUseCase(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                )
                _state.update { state ->
                    state.copy(hourlyForecast = hourlyForecast)
                }
            } catch (e: Exception) {
                Log.e(ForecastViewModel::class.simpleName, e.localizedMessage ?: UNKNOWN_ERROR)
            }
        }
    }

    private fun loadDailyForecast() {
        viewModelScope.launch {
            try {
                val dailyForecast = getDailyForecastUseCase(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                )
                _state.update { state ->
                    state.copy(dailyForecast = dailyForecast)
                }
            } catch (e: Exception) {
                Log.e(ForecastViewModel::class.simpleName, e.localizedMessage ?: UNKNOWN_ERROR)
            }
        }
    }

    private companion object {
        const val LATITUDE: Float = 47.50f
        const val LONGITUDE: Float = 19.04f

        const val UNKNOWN_ERROR = "unknown error"
    }
}