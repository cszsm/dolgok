package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.forecast.data.ForecastRepository
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.transformers.ForecastTransformer

class GetDailyForecastUseCase(
    private val forecastRepository: ForecastRepository,
    private val forecastTransformer: ForecastTransformer,
) {
    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
    ): DailyForecast? {
        return forecastRepository
            .fetchDailyForecast(
                latitude = latitude,
                longitude = longitude,
            )
            .let(forecastTransformer::transformDaily)
    }
}