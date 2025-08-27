package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.usecases.GetCurrentTimeUseCase
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository

class FetchFirstDayHourlyForecastUseCase(
    private val calculateForecastDayIntervalUseCase: CalculateForecastDayIntervalUseCase,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
    private val forecastRepository: ForecastRepository,
) {

    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
    ) {
        val firstBatchTimeInterval = calculateForecastDayIntervalUseCase(
            from = getCurrentTimeUseCase()
        )
        forecastRepository.fetchHourlyForecast(
            latitude = latitude,
            longitude = longitude,
            timeInterval = firstBatchTimeInterval
        )
    }
}