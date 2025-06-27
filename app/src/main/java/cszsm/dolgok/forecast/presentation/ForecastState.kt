package cszsm.dolgok.forecast.presentation

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast

data class ForecastState(
    val selectedTimeResolution: TimeResolution = TimeResolution.HOURLY,
    val selectedWeatherVariable: WeatherVariable = WeatherVariable.TEMPERATURE,

    val hourlyForecast: Result<HourlyForecast, DataError>? = null,
    val dailyForecast: Result<DailyForecast, DataError>? = null,
)