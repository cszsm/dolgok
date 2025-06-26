package cszsm.dolgok.forecast.data

import kotlinx.datetime.LocalDateTime

class ForecastRepository(
    private val weatherDataSource: WeatherDataSource,
) {

    suspend fun fetchHourlyForecast(
        latitude: Float,
        longitude: Float,
        startHour: LocalDateTime,
        endHour: LocalDateTime,
    ): ForecastApiModel? =
        weatherDataSource.getHourlyForecast(
            latitude = latitude,
            longitude = longitude,
            startHour = startHour,
            endHour = endHour
        )

    suspend fun fetchDailyForecast(
        latitude: Float,
        longitude: Float,
    ): ForecastApiModel? =
        weatherDataSource.getDailyForecast(
            latitude = latitude,
            longitude = longitude,
        )
}