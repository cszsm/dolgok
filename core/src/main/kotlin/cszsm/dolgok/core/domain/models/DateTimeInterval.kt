package cszsm.dolgok.core.domain.models

import kotlinx.datetime.LocalDateTime

data class DateTimeInterval(
    val start: LocalDateTime,
    val end: LocalDateTime,
)