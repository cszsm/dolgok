package cszsm.dolgok.domain.dto

import kotlinx.datetime.LocalDateTime

data class DateTimeInterval(
    val start: LocalDateTime,
    val end: LocalDateTime,
)
