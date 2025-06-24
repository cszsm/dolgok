package cszsm.dolgok.data.dto

import kotlinx.serialization.Serializable

// TODO: check how fields can be optional
@Serializable
data class ForecastApiModel(
    val hourly: ForecastDataApiModel,
)

@Serializable
data class ForecastDataApiModel(
    val time: List<String>,
    val temperature_2m: List<Float>,
    val rain: List<Float>,
    val surface_pressure: List<Float>,
)