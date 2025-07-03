package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.data.ForecastRepository
import cszsm.dolgok.forecast.domain.models.DailyForecast
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetDailyForecastUseCaseTest {

    private val mockForecastRepository: ForecastRepository = mockk()

    private lateinit var getDailyForecastUseCase: GetDailyForecastUseCase

    @BeforeEach
    fun setUp() {
        getDailyForecastUseCase = GetDailyForecastUseCase(
            forecastRepository = mockForecastRepository,
        )
    }

    @Test
    fun invoke() = runTest {
        // Given
        coEvery {
            mockForecastRepository.fetchDailyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
            )
        } returns RESULT

        // When
        val actual = getDailyForecastUseCase(
            latitude = LATITUDE,
            longitude = LONGITUDE,
        )

        // Then
        val expected = RESULT
        assertEquals(expected, actual)
    }

    private companion object {
        const val LATITUDE = 47.5f
        const val LONGITUDE = 19.04f

        val RESULT: Result.Success<DailyForecast, DataError> = mockk()
    }
}