package cszsm.dolgok.forecast.data

import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import kotlinx.datetime.LocalDateTime

class ForecastRepository(
    private val weatherDataSource: WeatherDataSource,
    private val forecastTransformer: ForecastTransformer,
) {

    suspend fun fetchHourlyForecast(
        latitude: Float,
        longitude: Float,
        startHour: LocalDateTime,
        endHour: LocalDateTime,
    ): HourlyForecast? =
        weatherDataSource.getHourlyForecast(
            latitude = latitude,
            longitude = longitude,
            startHour = startHour,
            endHour = endHour
        )?.let(forecastTransformer::transformHourly)

    suspend fun fetchDailyForecast(
        latitude: Float,
        longitude: Float,
    ): DailyForecast? =
        weatherDataSource.getDailyForecast(
            latitude = latitude,
            longitude = longitude,
        )?.let(forecastTransformer::transformDaily)
}