package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.util.plus
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class FetchMoreHourlyForecastUseCase(
    private val calculateForecastDayIntervalUseCase: CalculateForecastDayIntervalUseCase,
    private val forecastRepository: ForecastRepository,
) {

    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
        lastLoadedDateTime: LocalDateTime,
    ) {
        val nextBatchStartDateTime = lastLoadedDateTime
            .toInstant(timeZone = TimeZone.currentSystemDefault())
            .plus(hours = 1)
        val nextBatchTimeInterval = calculateForecastDayIntervalUseCase(
            from = nextBatchStartDateTime
        )
        forecastRepository.fetchHourlyForecast(
            latitude = latitude,
            longitude = longitude,
            timeInterval = nextBatchTimeInterval,
        )
    }
}