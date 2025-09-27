package cszsm.dolgok.forecast.presentation

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.ForecastDay
import cszsm.dolgok.forecast.domain.models.HourlyForecast

internal data class ForecastState(
    val selectedTimeResolution: TimeResolution = TimeResolution.HOURLY,
    val selectedWeatherVariable: WeatherVariable = WeatherVariable.TEMPERATURE,

    val hourlyForecastByDay: Map<ForecastDay, Result<HourlyForecast, DataError>> = emptyMap(),
    val hourlyForecastLoading: Boolean = false,
    val dailyForecast: Result<DailyForecast, DataError>? = null,
    val dailyForecastLoading: Boolean = false,
) {
    // TODO: improve error handling
    val hourlyForecast: Result<HourlyForecast, DataError>
        get() = hourlyForecastByDay
            .flatMap {
                if (it.value is Result.Failure<*, *>) return it.value
                val forecast =
                    (it.value as? Result.Success<HourlyForecast, DataError>) ?: return it.value

                return@flatMap forecast.data.hours
            }.let {
                Result.Success(data = HourlyForecast(hours = it))
            }
}