package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.models.DateInterval
import cszsm.dolgok.core.domain.usecases.GetCurrentTimeUseCase
import cszsm.dolgok.core.util.plus
import cszsm.dolgok.core.util.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class CalculateDailyForecastIntervalUseCase(
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
) {

    operator fun invoke(): DateInterval {
        val now = getCurrentTimeUseCase()

        return DateInterval(
            start = now.toLocalDateTime().date,
            end = now.plus(ALLOWED_DAYS - 1).toLocalDateTime().date,
        )
    }

    private companion object {
        const val ALLOWED_DAYS = 10
    }
}