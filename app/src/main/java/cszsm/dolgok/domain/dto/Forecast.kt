package cszsm.dolgok.domain.dto

import kotlinx.datetime.LocalDateTime

data class Forecast(
    val hourly: List<ForecastUnit>
)

data class ForecastUnit(
    val time: LocalDateTime?,
    val temperature: Float?,
)