package cszsm.dolgok.domain.dto

data class HourlyForecast(
    val hours: List<HourlyForecastUnit>?,
)

data class DailyForecast(
    val days: List<DailyForecastUnit>?,
)
