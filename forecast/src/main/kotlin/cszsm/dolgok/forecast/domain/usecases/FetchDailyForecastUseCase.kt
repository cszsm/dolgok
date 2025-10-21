package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.forecast.domain.repositories.ForecastRepository

internal class FetchDailyForecastUseCase(
    private val forecastRepository: ForecastRepository,
) {

    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
    ) {
        forecastRepository.fetchDailyForecast(
            latitude = latitude,
            longitude = longitude,
        )
    }
}