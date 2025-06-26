package cszsm.dolgok.forecast.data

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