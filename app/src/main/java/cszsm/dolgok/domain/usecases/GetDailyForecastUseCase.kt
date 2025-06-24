package cszsm.dolgok.domain.usecases

import cszsm.dolgok.data.ForecastRepository
import cszsm.dolgok.domain.dto.DailyForecast
import cszsm.dolgok.domain.transformers.ForecastTransformer

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