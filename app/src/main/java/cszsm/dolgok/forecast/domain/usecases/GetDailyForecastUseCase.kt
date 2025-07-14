package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository

class GetDailyForecastUseCase(
    private val forecastRepository: ForecastRepository,
) {
    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
    ): Result<DailyForecast, DataError> {
        return forecastRepository
            .fetchDailyForecast(
                latitude = latitude,
                longitude = longitude,
            )
    }
}