package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.models.DateInterval
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FetchDailyForecastUseCaseTest {

    private val mockCalculateDailyForecastIntervalUseCase: CalculateDailyForecastIntervalUseCase =
        mockk()
    private val mockForecastRepository: ForecastRepository = mockk(relaxed = true)

    private lateinit var fetchDailyForecastUseCase: FetchDailyForecastUseCase

    @BeforeEach
    fun setUp() {
        fetchDailyForecastUseCase = FetchDailyForecastUseCase(
            calculateDailyForecastIntervalUseCase = mockCalculateDailyForecastIntervalUseCase,
            forecastRepository = mockForecastRepository,
        )
    }

    @Test
    fun invoke() = runTest {
        // Given
        every { mockCalculateDailyForecastIntervalUseCase() } returns DATE_INTERVAL

        // When
        fetchDailyForecastUseCase(
            latitude = LATITUDE,
            longitude = LONGITUDE,
        )

        // Then
        coVerify {
            mockForecastRepository.fetchDailyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                dateInterval = DATE_INTERVAL,
            )
        }
    }

    private companion object {
        val DATE_INTERVAL: DateInterval = mockk()
        const val LATITUDE = 47.5f
        const val LONGITUDE = 19.04f
    }
}