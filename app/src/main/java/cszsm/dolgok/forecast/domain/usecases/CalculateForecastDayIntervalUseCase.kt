package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.forecast.domain.models.DateTimeInterval
import cszsm.dolgok.forecast.domain.models.ForecastDay
import cszsm.dolgok.core.util.minus
import cszsm.dolgok.core.util.plus
import cszsm.dolgok.core.util.roundDownToHour
import cszsm.dolgok.core.util.toLocalDateTime
import kotlinx.datetime.Clock

class CalculateForecastDayIntervalUseCase {

    operator fun invoke(
        forecastDay: ForecastDay,
    ): DateTimeInterval {
        val now = Clock.System.now()

        val forecastStart = now
            .plus(days = forecastDay.offset)

        val forecastEnd = forecastStart
            .plus(days = 1)
            .minus(hours = 1)

        return DateTimeInterval(
            start = forecastStart
                .toLocalDateTime()
                .roundDownToHour(),
            end = forecastEnd
                .toLocalDateTime()
                .roundDownToHour()
        )
    }
}