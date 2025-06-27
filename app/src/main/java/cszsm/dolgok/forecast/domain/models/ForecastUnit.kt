package cszsm.dolgok.forecast.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class HourlyForecastUnit(
    val time: LocalDateTime,
    val temperature: Float,
    val rain: Float,
    val pressure: Float,
)

data class DailyForecastUnit(
    val date: LocalDate,
    val temperatureMax: Float,
    val temperatureMin: Float,
    val rainSum: Float,
)