package cszsm.dolgok.core.domain.usecases

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class GetCurrentTimeUseCase(
    private val clock: Clock,
) {

    operator fun invoke(): Instant = clock.now()
}