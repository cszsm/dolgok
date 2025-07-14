package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.domain.models.DateTimeInterval
import cszsm.dolgok.forecast.domain.models.ForecastDay
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetHourlyForecastUseCaseTest {

    private val mockForecastRepository: ForecastRepository = mockk()
    private val mockCalculateForecastDayIntervalUseCase: CalculateForecastDayIntervalUseCase =
        mockk()

    private lateinit var getHourlyForecastUseCase: GetHourlyForecastUseCase

    @BeforeEach
    fun setUp() {
        getHourlyForecastUseCase = GetHourlyForecastUseCase(
            forecastRepository = mockForecastRepository,
            calculateForecastDayIntervalUseCase = mockCalculateForecastDayIntervalUseCase,
        )
    }

    @Test
    fun invoke() = runTest {
        // Given
        every {
            mockCalculateForecastDayIntervalUseCase(forecastDay = FORECAST_DAY)
        } returns INTERVAL
        coEvery {
            mockForecastRepository.fetchHourlyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                startHour = START_HOUR,
                endHour = END_HOUR,
            )
        } returns RESULT

        // When
        val actual = getHourlyForecastUseCase(
            latitude = LATITUDE,
            longitude = LONGITUDE,
            forecastDay = FORECAST_DAY,
        )

        // Then
        val expected = RESULT
        assertEquals(expected, actual)
    }

    private companion object {
        const val LATITUDE = 47.5f
        const val LONGITUDE = 19.04f
        val FORECAST_DAY = ForecastDay.TODAY
        val START_HOUR = LocalDateTime.parse("2025-06-27T13:00")
        val END_HOUR = LocalDateTime.parse("2025-06-28T12:00")
        val INTERVAL = DateTimeInterval(
            start = START_HOUR,
            end = END_HOUR
        )

        val RESULT: Result.Success<HourlyForecast, DataError> = mockk()
    }
}