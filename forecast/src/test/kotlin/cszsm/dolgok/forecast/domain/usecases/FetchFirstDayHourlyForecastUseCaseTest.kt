package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.models.DateTimeInterval
import cszsm.dolgok.core.domain.usecases.GetCurrentTimeUseCase
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class FetchFirstDayHourlyForecastUseCaseTest {

    private val mockCalculateNextDayIntervalUseCase: CalculateNextDayIntervalUseCase = mockk()
    private val mockGetCurrentTimeUseCase: GetCurrentTimeUseCase = mockk()
    private val mockForecastRepository: ForecastRepository = mockk(relaxed = true)

    private lateinit var fetchFirstDayHourlyForecastUseCase: FetchFirstDayHourlyForecastUseCase

    @BeforeEach
    fun setUp() {
        fetchFirstDayHourlyForecastUseCase = FetchFirstDayHourlyForecastUseCase(
            calculateNextDayIntervalUseCase = mockCalculateNextDayIntervalUseCase,
            getCurrentTimeUseCase = mockGetCurrentTimeUseCase,
            forecastRepository = mockForecastRepository,
        )
    }

    @Test
    fun invoke() = runTest {
        // Given
        every { mockGetCurrentTimeUseCase() } returns INSTANT
        every { mockCalculateNextDayIntervalUseCase(from = INSTANT) } returns DATE_TIME_INTERVAL

        // When
        fetchFirstDayHourlyForecastUseCase(
            latitude = LATITUDE,
            longitude = LONGITUDE
        )

        // Then
        verify { mockGetCurrentTimeUseCase() }
        verify { mockCalculateNextDayIntervalUseCase(from = INSTANT) }
        coVerify {
            mockForecastRepository.fetchHourlyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                timeInterval = DATE_TIME_INTERVAL,
            )
        }
    }

    private companion object {
        val INSTANT: Instant = mockk()
        val DATE_TIME_INTERVAL: DateTimeInterval = mockk()
        const val LATITUDE = 47.5f
        const val LONGITUDE = 19.04f
    }
}