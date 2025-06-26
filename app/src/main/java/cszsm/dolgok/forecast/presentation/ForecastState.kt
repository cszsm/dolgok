package cszsm.dolgok.forecast.presentation

import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast

data class ForecastState(
    val selectedTimeResolution: TimeResolution = TimeResolution.HOURLY,
    val selectedWeatherVariable: WeatherVariable = WeatherVariable.TEMPERATURE,

    val hourlyForecast: HourlyForecast? = null,
    val dailyForecast: DailyForecast? = null,
)