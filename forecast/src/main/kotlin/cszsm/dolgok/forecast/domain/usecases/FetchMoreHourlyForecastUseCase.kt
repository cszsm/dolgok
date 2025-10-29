package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.util.plus
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class FetchMoreHourlyForecastUseCase(
    private val calculateNextDayIntervalUseCase: CalculateNextDayIntervalUseCase,
    private val forecastRepository: ForecastRepository,
) {

    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
        lastLoadedDateTime: LocalDateTime,
    ) {
        // TODO: this logic might be better in a UseCase for more proper testing
        val nextBatchStartDateTime = lastLoadedDateTime
            .toInstant(timeZone = TimeZone.currentSystemDefault())
            .plus(hours = 1)
        val nextBatchTimeInterval = calculateNextDayIntervalUseCase(
            from = nextBatchStartDateTime
        )
        forecastRepository.fetchHourlyForecast(
            latitude = latitude,
            longitude = longitude,
            timeInterval = nextBatchTimeInterval,
        )
    }
}