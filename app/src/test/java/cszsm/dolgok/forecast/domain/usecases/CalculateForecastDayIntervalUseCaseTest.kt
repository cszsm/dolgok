package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.usecases.GetCurrentTimeUseCase
import cszsm.dolgok.forecast.domain.models.DateTimeInterval
import cszsm.dolgok.forecast.domain.models.ForecastDay
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.FieldSource

class CalculateForecastDayIntervalUseCaseTest {

    private val mockGetCurrentTimeUseCase: GetCurrentTimeUseCase = mockk()

    private lateinit var calculateForecastDayIntervalUseCase: CalculateForecastDayIntervalUseCase

    @BeforeEach
    fun setUp() {
        calculateForecastDayIntervalUseCase = CalculateForecastDayIntervalUseCase(
            getCurrentTimeUseCase = mockGetCurrentTimeUseCase,
        )

        every { mockGetCurrentTimeUseCase() } returns TEST_TIME
    }

    @ParameterizedTest
    @FieldSource("testData")
    fun invoke(
        data: Pair<ForecastDay, DateTimeInterval>,
    ) {
        // Given
        val forecastDay = data.first

        // When
        val actual = calculateForecastDayIntervalUseCase(forecastDay = forecastDay)

        // Then
        val expected = data.second
        assertEquals(expected, actual)
    }

    private companion object {
        val TEST_TIME = Instant.parse("2025-07-04T11:22:56.914640Z")

        @Suppress("unused")
        val testData = listOf(
            Pair(
                ForecastDay.TODAY,
                DateTimeInterval(
                    start = LocalDateTime(
                        date = LocalDate(year = 2025, monthNumber = 7, dayOfMonth = 4),
                        time = LocalTime(hour = 13, minute = 0, second = 0, nanosecond = 0),
                    ),
                    end = LocalDateTime(
                        date = LocalDate(year = 2025, monthNumber = 7, dayOfMonth = 5),
                        time = LocalTime(hour = 12, minute = 0, second = 0, nanosecond = 0),
                    ),
                ),
            ),
            Pair(
                ForecastDay.TOMORROW,
                DateTimeInterval(
                    start = LocalDateTime(
                        date = LocalDate(year = 2025, monthNumber = 7, dayOfMonth = 5),
                        time = LocalTime(hour = 13, minute = 0, second = 0, nanosecond = 0),
                    ),
                    end = LocalDateTime(
                        date = LocalDate(year = 2025, monthNumber = 7, dayOfMonth = 6),
                        time = LocalTime(hour = 12, minute = 0, second = 0, nanosecond = 0),
                    ),
                ),
            ),
            Pair(
                ForecastDay.THE_DAY_AFTER_TOMORROW,
                DateTimeInterval(
                    start = LocalDateTime(
                        date = LocalDate(year = 2025, monthNumber = 7, dayOfMonth = 6),
                        time = LocalTime(hour = 13, minute = 0, second = 0, nanosecond = 0),
                    ),
                    end = LocalDateTime(
                        date = LocalDate(year = 2025, monthNumber = 7, dayOfMonth = 7),
                        time = LocalTime(hour = 12, minute = 0, second = 0, nanosecond = 0),
                    ),
                ),
            ),
        )
    }
}