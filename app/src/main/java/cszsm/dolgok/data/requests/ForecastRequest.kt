package cszsm.dolgok.data.requests

import io.ktor.resources.Resource

@Suppress("PropertyName", "unused")
@Resource("/forecast")
class ForecastRequest(
    val latitude: String = "47.50",
    val longitude: String = "19.04",
    val timezone: String = "auto",
    val hourly: String = "temperature_2m",
    val forecast_days: String = "1",
)