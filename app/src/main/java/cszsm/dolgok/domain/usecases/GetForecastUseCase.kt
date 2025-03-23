package cszsm.dolgok.domain.usecases

import cszsm.dolgok.data.WeatherService
import cszsm.dolgok.domain.dto.Forecast
import cszsm.dolgok.domain.enums.ForecastDay
import cszsm.dolgok.domain.transformers.ForecastTransformer

class GetForecastUseCase(
    private val weatherService: WeatherService,
    private val forecastTransformer: ForecastTransformer,
    private val calculateForecastDayIntervalUseCase: CalculateForecastDayIntervalUseCase,
) {
    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
        forecastDay: ForecastDay = ForecastDay.TODAY,
    ): Forecast? {
        val interval = calculateForecastDayIntervalUseCase(forecastDay = forecastDay)

        return weatherService
            .getForecast(
                latitude = latitude,
                longitude = longitude,
                startHour = interval.start,
                endHour = interval.end,
            )
            .let(forecastTransformer::transform)
    }
}
