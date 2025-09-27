package cszsm.dolgok.forecast.domain.models

import kotlinx.datetime.LocalDateTime

internal data class HourlyForecast(
    val hours: List<HourlyForecastUnit>,
)

internal data class HourlyForecastUnit(
    val time: LocalDateTime,
    val temperature: Float,
    val rain: Float,
    val pressure: Float,
)