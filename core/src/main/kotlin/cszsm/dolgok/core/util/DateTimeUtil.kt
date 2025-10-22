package cszsm.dolgok.core.util

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

fun Instant.plus(days: Int = 0, hours: Int = 0) =
    plus(Duration.parse(DateTimePeriod(days = days, hours = hours).toString()))

fun Instant.minus(days: Int = 0, hours: Int = 0) =
    minus(Duration.parse(DateTimePeriod(days = days, hours = hours).toString()))

fun Instant.toLocalDateTime() = toLocalDateTime(timeZone = TimeZone.currentSystemDefault())

fun LocalDateTime.roundDownToHour() = LocalDateTime(
    year = year,
    month = month,
    dayOfMonth = dayOfMonth,
    hour = hour,
    minute = 0,
    second = 0,
    nanosecond = 0,
)

fun LocalDateTime.plus(hours: Int) = this
    .toInstant(timeZone = TimeZone.currentSystemDefault())
    .plus(hours = hours)
    .toLocalDateTime()