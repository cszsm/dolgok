package cszsm.dolgok.forecast.presentation

import androidx.compose.runtime.Immutable
import cszsm.dolgok.core.domain.models.FetchedData
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast

@Immutable
internal data class ForecastScreenState(
    val selectedTimeResolution: TimeResolution = TimeResolution.HOURLY,
    val selectedWeatherVariable: WeatherVariable = WeatherVariable.TEMPERATURE,

    val hourlyForecast: FetchedData<HourlyForecast> = FetchedData(),
    val dailyForecast: FetchedData<DailyForecast> = FetchedData(),
)