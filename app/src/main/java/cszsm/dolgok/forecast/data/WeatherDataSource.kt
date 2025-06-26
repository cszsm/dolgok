package cszsm.dolgok.forecast.data

import cszsm.dolgok.forecast.data.models.ForecastApiModel
import kotlinx.datetime.LocalDateTime

interface WeatherDataSource {

    suspend fun getHourlyForecast(
        latitude: Float,
        longitude: Float,
        startHour: LocalDateTime,
        endHour: LocalDateTime,
    ): ForecastApiModel?

    suspend fun getDailyForecast(
        latitude: Float,
        longitude: Float,
    ): ForecastApiModel?
}