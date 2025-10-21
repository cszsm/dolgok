package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.usecases.GetCurrentTimeUseCase
import cszsm.dolgok.core.util.plus
import cszsm.dolgok.core.util.toLocalDateTime
import kotlinx.datetime.LocalDateTime

internal class IsMoreForecastAllowedUseCase(
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
) {

    operator fun invoke(
        lastLoadedForecastDateTime: LocalDateTime,
    ): Boolean {
        val now = getCurrentTimeUseCase()
        val allowedDaysLater = now.plus(days = ALLOWED_DAYS - 1)

        return lastLoadedForecastDateTime < allowedDaysLater.toLocalDateTime()
    }

    private companion object {
        const val ALLOWED_DAYS = 3
    }
}