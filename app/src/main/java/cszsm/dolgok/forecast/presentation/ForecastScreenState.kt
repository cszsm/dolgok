package cszsm.dolgok.forecast.presentation

import androidx.compose.runtime.Immutable
import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast

data class ForecastScreenState(
    val selectedTimeResolution: TimeResolution = TimeResolution.HOURLY,
    val selectedWeatherVariable: WeatherVariable = WeatherVariable.TEMPERATURE,

    val hourlyForecastState: ForecastState<HourlyForecast> = ForecastState(),
    val dailyForecastState: ForecastState<DailyForecast> = ForecastState(),
) {

    // TODO: switch to LoadedData?
    @Immutable
    data class ForecastState<T>(
        val forecast: T? = null,
        val loading: Boolean = false,
        val error: DataError? = null,
    )
}