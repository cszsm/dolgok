package cszsm.dolgok.domain.usecases

import cszsm.dolgok.domain.dto.DateTimeInterval
import cszsm.dolgok.domain.enums.ForecastDay
import cszsm.dolgok.util.minus
import cszsm.dolgok.util.plus
import cszsm.dolgok.util.roundDownToHour
import cszsm.dolgok.util.toLocalDateTime
import kotlinx.datetime.Clock

class CalculateForecastDayIntervalUseCase() {

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