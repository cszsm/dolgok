package cszsm.dolgok.forecast.domain.models

import kotlinx.datetime.LocalDate

internal data class DailyForecast(
    val days: Map<LocalDate, Variables>,
) {
    data class Variables(
        val temperatureMax: Float,
        val temperatureMin: Float,
        val rainSum: Float,
    )
}