package cszsm.dolgok.data

import cszsm.dolgok.data.dto.ForecastApiModel
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