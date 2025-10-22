package cszsm.dolgok.forecast.domain.models

import kotlinx.datetime.LocalDate

internal data class DailyForecast(
    val days: Map<LocalDate, Variables>,
    val units: Units,
) {
    data class Variables(
        val temperatureMax: Float,
        val temperatureMin: Float,
        val rainSum: Float,
    )

    data class Units(
        val temperatureMax: String,
        val temperatureMin: String,
        val rainSum: String,
    )
}