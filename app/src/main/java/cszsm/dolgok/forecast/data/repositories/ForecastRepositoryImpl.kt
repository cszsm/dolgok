package cszsm.dolgok.forecast.data.repositories

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.data.transformers.ForecastTransformer
import cszsm.dolgok.forecast.data.datasources.WeatherDataSource
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import kotlinx.datetime.LocalDateTime

class ForecastRepositoryImpl(
    private val weatherDataSource: WeatherDataSource,
    private val forecastTransformer: ForecastTransformer,
) : ForecastRepository {

    override suspend fun fetchHourlyForecast(
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

    override suspend fun fetchDailyForecast(
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