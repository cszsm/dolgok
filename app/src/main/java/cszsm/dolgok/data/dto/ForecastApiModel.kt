package cszsm.dolgok.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForecastApiModel(
    val hourly: ForecastDataApiModel? = null,
    val daily: ForecastDataApiModel? = null,
)

@Serializable
data class ForecastDataApiModel(
    val time: List<String>,
    val temperature_2m: List<Float>? = null,
    val temperature_2m_max: List<Float>? = null,
    val temperature_2m_min: List<Float>? = null,
    val rain: List<Float>? = null,
    val rain_sum: List<Float>? = null,
    val surface_pressure: List<Float>? = null,
)