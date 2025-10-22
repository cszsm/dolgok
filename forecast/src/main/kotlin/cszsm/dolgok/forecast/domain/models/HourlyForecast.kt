package cszsm.dolgok.forecast.domain.models

import kotlinx.datetime.LocalDateTime

internal data class HourlyForecast(
    val hours: Map<LocalDateTime, Variables>,
    val units: Units,
) {
    data class Variables(
        val temperature: Float,
        val rain: Float,
        val pressure: Float,
    )

    data class Units(
        val temperature: String,
        val rain: String,
        val pressure: String,
    )
}