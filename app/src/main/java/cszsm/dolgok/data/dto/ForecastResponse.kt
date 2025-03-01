package cszsm.dolgok.data.dto

import kotlinx.serialization.Serializable

// TODO: check how fields can be optional
@Serializable
data class ForecastResponse(
    val hourly: TimeTemperatureResponse,
)

@Serializable
data class TimeTemperatureResponse(
    val time: List<String>,
    val temperature_2m: List<Float>,
)