package cszsm.dolgok.forecast.data.models

import kotlinx.serialization.Serializable

@Serializable
internal data class DailyForecastApiModel(
    val daily: Variables,
    val daily_units: Units,
) {
    @Serializable
    data class Variables(
        val time: List<String>,
        val temperature_2m_max: List<Float>,
        val temperature_2m_min: List<Float>,
        val rain_sum: List<Float>,
    )

    @Serializable
    data class Units(
        val temperature_2m_max: String,
        val temperature_2m_min: String,
        val rain_sum: String,
    )
}