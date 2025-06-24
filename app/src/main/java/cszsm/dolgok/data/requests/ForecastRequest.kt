package cszsm.dolgok.data.requests

import io.ktor.resources.Resource
import kotlinx.datetime.LocalDateTime

@Suppress("PropertyName", "unused")
@Resource("/forecast")
class ForecastRequest(
    val latitude: Float,
    val longitude: Float,
    val timezone: String = "auto",
    val hourly: String = "temperature_2m,rain,surface_pressure",
    val start_hour: LocalDateTime,
    val end_hour: LocalDateTime,
)