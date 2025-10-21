package cszsm.dolgok.forecast.data.models

import kotlinx.serialization.Serializable

@Serializable
internal data class DailyForecastApiModel(
    val daily: Variables,
) {
    @Serializable
    data class Variables(
        val time: List<String>,
        val temperature_2m_max: List<Float>,
        val temperature_2m_min: List<Float>,
        val rain_sum: List<Float>,
    )
}