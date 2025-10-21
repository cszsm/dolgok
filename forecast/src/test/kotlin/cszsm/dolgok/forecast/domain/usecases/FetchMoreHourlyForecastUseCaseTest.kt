package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.models.DateTimeInterval
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FetchMoreHourlyForecastUseCaseTest {

    private val mockCalculateNextDayIntervalUseCase: CalculateNextDayIntervalUseCase = mockk()
    private val mockForecastRepository: ForecastRepository = mockk(relaxed = true)

    private lateinit var fetchMoreHourlyForecastUseCase: FetchMoreHourlyForecastUseCase

    @BeforeEach
    fun setUp() {
        fetchMoreHourlyForecastUseCase = FetchMoreHourlyForecastUseCase(
            calculateNextDayIntervalUseCase = mockCalculateNextDayIntervalUseCase,
            forecastRepository = mockForecastRepository,
        )
    }

    @Test
    fun invoke() = runTest {
        // Given
        every { mockCalculateNextDayIntervalUseCase(from = any()) } returns DATE_TIME_INTERVAL

        // When
        fetchMoreHourlyForecastUseCase(
            latitude = LATITUDE,
            longitude = LONGITUDE,
            lastLoadedDateTime = LOCAL_DATE_TIME,
        )

        // Then
        verify { mockCalculateNextDayIntervalUseCase(from = any()) }
        coVerify {
            mockForecastRepository.fetchHourlyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                timeInterval = DATE_TIME_INTERVAL,
            )
        }
    }

    private companion object {
        val DATE_TIME_INTERVAL: DateTimeInterval = mockk()
        const val LATITUDE = 47.5f
        const val LONGITUDE = 19.04f
        val LOCAL_DATE_TIME: LocalDateTime = mockk(relaxed = true)
    }
}