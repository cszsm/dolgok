package cszsm.dolgok.forecast.domain.models

import kotlinx.datetime.LocalDateTime

internal data class DateTimeInterval(
    val start: LocalDateTime,
    val end: LocalDateTime,
)
