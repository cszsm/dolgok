package cszsm.dolgok.forecast.domain.usecases

import cszsm.dolgok.forecast.data.ForecastRepository
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.models.ForecastDay
import cszsm.dolgok.forecast.domain.transformers.ForecastTransformer

class GetHourlyForecastUseCase(
    private val forecastRepository: ForecastRepository,
    private val forecastTransformer: ForecastTransformer,
    private val calculateForecastDayIntervalUseCase: CalculateForecastDayIntervalUseCase,
) {
    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
        forecastDay: ForecastDay = ForecastDay.TODAY,
    ): HourlyForecast? {
        val interval = calculateForecastDayIntervalUseCase(forecastDay = forecastDay)

        return forecastRepository
            .fetchHourlyForecast(
                latitude = latitude,
                longitude = longitude,
                startHour = interval.start,
                endHour = interval.end,
            )
            .let(forecastTransformer::transformHourly)
    }
}
