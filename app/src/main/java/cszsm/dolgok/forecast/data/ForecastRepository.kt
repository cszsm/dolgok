package cszsm.dolgok.forecast.data

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
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
    ): Result<HourlyForecast, DataError> =
        try {
            weatherDataSource.getHourlyForecast(
                latitude = latitude,
                longitude = longitude,
                startHour = startHour,
                endHour = endHour
            ).let(forecastTransformer::transformHourly)
        } catch (e: Exception) {
            Result.Failure(error = DataError.NETWORK)
        }

    suspend fun fetchDailyForecast(
        latitude: Float,
        longitude: Float,
    ): Result<DailyForecast, DataError> =
        try {
            weatherDataSource.getDailyForecast(
                latitude = latitude,
                longitude = longitude,
            ).let(forecastTransformer::transformDaily)
        } catch (e: Exception) {
            Result.Failure(error = DataError.NETWORK)
        }
}