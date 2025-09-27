package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.domain.models.ForecastDay
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository

internal class GetHourlyForecastUseCase(
    private val forecastRepository: ForecastRepository,
    private val calculateForecastDayIntervalUseCase: CalculateForecastDayIntervalUseCase,
) {
    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
        forecastDay: ForecastDay,
    ): Result<HourlyForecast, DataError> {
        val interval = calculateForecastDayIntervalUseCase(forecastDay = forecastDay)

        return forecastRepository
            .fetchHourlyForecast(
                latitude = latitude,
                longitude = longitude,
                startHour = interval.start,
                endHour = interval.end,
            )
    }
}
