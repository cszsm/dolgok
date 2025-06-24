package cszsm.dolgok.ui.screens.forecast

import cszsm.dolgok.domain.dto.DailyForecast
import cszsm.dolgok.domain.dto.HourlyForecast

data class ForecastState(
    val selectedTimeResolution: TimeResolution = TimeResolution.HOURLY,
    val selectedWeatherVariable: WeatherVariable = WeatherVariable.TEMPERATURE,

    val hourlyForecast: HourlyForecast? = null,
    val dailyForecast: DailyForecast? = null,
)