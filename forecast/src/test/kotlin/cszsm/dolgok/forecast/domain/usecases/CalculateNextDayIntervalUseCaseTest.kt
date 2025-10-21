package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.models.DateTimeInterval
import cszsm.dolgok.core.domain.usecases.GetCurrentTimeUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CalculateNextDayIntervalUseCaseTest {

    private val mockGetCurrentTimeUseCase: GetCurrentTimeUseCase = mockk()

    private lateinit var calculateNextDayIntervalUseCase: CalculateNextDayIntervalUseCase

    @BeforeEach
    fun setUp() {
        calculateNextDayIntervalUseCase = CalculateNextDayIntervalUseCase()

        every { mockGetCurrentTimeUseCase() } returns TEST_TIME
    }

    @Test
    fun invoke() {
        // When
        val actual = calculateNextDayIntervalUseCase(from = TEST_TIME)

        // Then
        val expected = DateTimeInterval(
            start = LocalDateTime(
                date = LocalDate(year = 2025, monthNumber = 7, dayOfMonth = 4),
                time = LocalTime(hour = 13, minute = 0, second = 0, nanosecond = 0),
            ),
            end = LocalDateTime(
                date = LocalDate(year = 2025, monthNumber = 7, dayOfMonth = 5),
                time = LocalTime(hour = 12, minute = 0, second = 0, nanosecond = 0),
            ),
        )
        assertEquals(expected, actual)
    }

    private companion object {
        val TEST_TIME = Instant.parse("2025-07-04T11:22:56.914640Z")
    }
}