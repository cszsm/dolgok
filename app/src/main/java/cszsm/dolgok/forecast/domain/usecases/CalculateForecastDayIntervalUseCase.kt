package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.usecases.GetCurrentTimeUseCase
import cszsm.dolgok.core.util.minus
import cszsm.dolgok.core.util.plus
import cszsm.dolgok.core.util.roundDownToHour
import cszsm.dolgok.core.util.toLocalDateTime
import cszsm.dolgok.forecast.domain.models.DateTimeInterval
import cszsm.dolgok.forecast.domain.models.ForecastDay

class CalculateForecastDayIntervalUseCase(
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
) {

    operator fun invoke(
        forecastDay: ForecastDay,
    ): DateTimeInterval {
        val now = getCurrentTimeUseCase()

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