package cszsm.dolgok.core.domain.usecases

import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.Clock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetCurrentTimeUseCaseTest {

    private lateinit var getCurrentTimeUseCase: GetCurrentTimeUseCase

    @BeforeEach
    fun setUp() {
        getCurrentTimeUseCase = GetCurrentTimeUseCase(
            clock = CLOCK
        )
    }

    @Test
    fun invoke() {
        // When
        getCurrentTimeUseCase()

        // Then
        verify { CLOCK.now() }
    }

    private companion object {
        val CLOCK: Clock = mockk(relaxed = true)
    }
}
