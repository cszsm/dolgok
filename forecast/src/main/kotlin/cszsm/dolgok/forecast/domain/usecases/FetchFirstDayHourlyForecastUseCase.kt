package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.usecases.GetCurrentTimeUseCase
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository

internal class FetchFirstDayHourlyForecastUseCase(
    private val calculateNextDayIntervalUseCase: CalculateNextDayIntervalUseCase,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
    private val forecastRepository: ForecastRepository,
) {

    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
    ) {
        val firstBatchTimeInterval = calculateNextDayIntervalUseCase(
            from = getCurrentTimeUseCase()
        )
        forecastRepository.fetchHourlyForecast(
            latitude = latitude,
            longitude = longitude,
            timeInterval = firstBatchTimeInterval
        )
    }
}