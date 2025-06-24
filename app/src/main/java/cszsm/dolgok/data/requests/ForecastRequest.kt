package cszsm.dolgok.data.requests

import io.ktor.resources.Resource
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Suppress("PropertyName", "unused")
@Resource("/forecast")
class ForecastRequest(
    val latitude: Float,
    val longitude: Float,
    val timezone: String = "auto",
    val hourly: String? = null,
    val daily: String? = null,
    val start_hour: LocalDateTime? = null,
    val end_hour: LocalDateTime? = null,
    val start_date: LocalDate? = null,
    val end_date: LocalDate? = null,
)