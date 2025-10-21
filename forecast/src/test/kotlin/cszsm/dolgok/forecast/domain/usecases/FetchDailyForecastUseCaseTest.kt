package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FetchDailyForecastUseCaseTest {

    private val mockForecastRepository: ForecastRepository = mockk(relaxed = true)

    private lateinit var fetchDailyForecastUseCase: FetchDailyForecastUseCase

    @BeforeEach
    fun setUp() {
        fetchDailyForecastUseCase = FetchDailyForecastUseCase(
            forecastRepository = mockForecastRepository,
        )
    }

    @Test
    fun invoke() = runTest {
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
            )
        }
    }

    private companion object {
        const val LATITUDE = 47.5f
        const val LONGITUDE = 19.04f
    }
}