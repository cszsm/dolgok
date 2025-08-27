package cszsm.dolgok.core.domain.model

import kotlinx.datetime.LocalDateTime

data class DateTimeInterval(
    val start: LocalDateTime,
    val end: LocalDateTime,
)
