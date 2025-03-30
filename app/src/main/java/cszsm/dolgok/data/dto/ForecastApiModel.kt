package cszsm.dolgok.data.dto

import kotlinx.serialization.Serializable

// TODO: check how fields can be optional
@Serializable
data class ForecastApiModel(
    val hourly: TimeTemperatureApiModel,
)

@Serializable
data class TimeTemperatureApiModel(
    val time: List<String>,
    val temperature_2m: List<Float>,
)