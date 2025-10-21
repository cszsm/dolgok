package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.models.DateTimeInterval
import cszsm.dolgok.core.util.minus
import cszsm.dolgok.core.util.plus
import cszsm.dolgok.core.util.roundDownToHour
import cszsm.dolgok.core.util.toLocalDateTime
import kotlinx.datetime.Instant

internal class CalculateNextDayIntervalUseCase {

    operator fun invoke(
        from: Instant,
    ): DateTimeInterval {
        val to = from
            .plus(days = 1)
            .minus(hours = 1)

        return DateTimeInterval(
            start = from.toLocalDateTime().roundDownToHour(),
            end = to.toLocalDateTime().roundDownToHour()
        )
    }
}