package cszsm.dolgok.core.presentation

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.time.format.TextStyle


fun Float.asTemperature() = "$this ${Typography.degree}C"

fun Float.asRain() = "$this mm"

fun Float.asPressure() = "$this hPa"

fun LocalDate.asLocalizedDayOfWeek(): String =
    dayOfWeek.getDisplayName(TextStyle.FULL, Locale.current.platformLocale)

fun LocalDateTime.asLocalizedDayOfWeek(): String =
    dayOfWeek.getDisplayName(TextStyle.FULL, Locale.current.platformLocale)