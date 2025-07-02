package cszsm.dolgok.forecast.domain.models

import kotlinx.datetime.LocalDateTime

data class HourlyForecast(
    val hours: List<HourlyForecastUnit>,
)

data class HourlyForecastUnit(
    val time: LocalDateTime,
    val temperature: Float,
    val rain: Float,
    val pressure: Float,
)