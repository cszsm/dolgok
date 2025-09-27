package cszsm.dolgok.forecast.domain.models

import kotlinx.datetime.LocalDate

internal data class DailyForecast(
    val days: List<DailyForecastUnit>,
)

internal data class DailyForecastUnit(
    val date: LocalDate,
    val temperatureMax: Float,
    val temperatureMin: Float,
    val rainSum: Float,
)