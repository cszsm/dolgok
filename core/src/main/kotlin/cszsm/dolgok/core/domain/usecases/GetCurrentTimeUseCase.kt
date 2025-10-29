package cszsm.dolgok.core.domain.usecases

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class GetCurrentTimeUseCase(
    private val clock: Clock,
) {

    operator fun invoke(): Instant = clock.now()
}