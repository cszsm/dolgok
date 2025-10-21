package cszsm.dolgok.forecast.domain.models

import kotlinx.datetime.LocalDateTime

internal data class HourlyForecast(
    val hours: Map<LocalDateTime, Variables>,
) {
    data class Variables(
        val temperature: Float,
        val rain: Float,
        val pressure: Float,
    )
}