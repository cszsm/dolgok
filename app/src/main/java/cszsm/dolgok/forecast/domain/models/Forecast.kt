package cszsm.dolgok.forecast.domain.models

data class HourlyForecast(
    val hours: List<HourlyForecastUnit>?,
)

data class DailyForecast(
    val days: List<DailyForecastUnit>?,
)
