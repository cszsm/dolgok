package cszsm.dolgok.data

import cszsm.dolgok.data.dto.ForecastApiModel
import kotlinx.datetime.LocalDateTime

class ForecastRepository(
    private val weatherDataSource: WeatherDataSource,
) {

    suspend fun fetchForecast(
        latitude: Float,
        longitude: Float,
        startHour: LocalDateTime,
        endHour: LocalDateTime,
    ): ForecastApiModel? =
        weatherDataSource.getForecast(
            latitude = latitude,
            longitude = longitude,
            startHour = startHour,
            endHour = endHour
        )
}