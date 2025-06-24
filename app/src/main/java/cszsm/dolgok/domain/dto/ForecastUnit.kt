package cszsm.dolgok.domain.dto

import kotlinx.datetime.LocalDateTime

data class ForecastUnit(
    val time: LocalDateTime?,
    val temperature: Float?,
    val rain: Float?,
    val pressure: Float?,
)
