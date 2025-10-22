package cszsm.dolgok.forecast.data.models

import kotlinx.serialization.Serializable

@Serializable
internal data class HourlyForecastApiModel(
    val hourly: Variables,
    val hourly_units: Units,
) {
    @Serializable
    data class Variables(
        val time: List<String>,
        val temperature_2m: List<Float>,
        val rain: List<Float>,
        val surface_pressure: List<Float>,
    )

    @Serializable
    data class Units(
        val temperature_2m: String,
        val rain: String,
        val surface_pressure: String,
    )
}
