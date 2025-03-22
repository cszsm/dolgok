package cszsm.dolgok.domain.usecases

import cszsm.dolgok.data.WeatherService
import cszsm.dolgok.domain.transformers.ForecastTransformer

class GetForecastUseCase(
    private val weatherService: WeatherService,
    private val forecastTransformer: ForecastTransformer,
) {
    suspend operator fun invoke() = weatherService
        .getForecast()
        .let(forecastTransformer::transform)
}