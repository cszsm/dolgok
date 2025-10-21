package cszsm.dolgok.forecast.data.models

import kotlinx.serialization.Serializable

@Serializable
internal data class HourlyForecastApiModel(
    val hourly: Variables,
) {
    @Serializable
    data class Variables(
        val time: List<String>,
        val temperature_2m: List<Float>,
        val rain: List<Float>,
        val surface_pressure: List<Float>,
    )
}
