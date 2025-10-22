package cszsm.dolgok.forecast.presentation

import androidx.compose.runtime.Immutable
import cszsm.dolgok.core.domain.models.FetchedData
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast

internal data class ForecastScreenState(
    val selectedTimeResolution: TimeResolution = TimeResolution.HOURLY,
    val selectedWeatherVariable: WeatherVariable = WeatherVariable.TEMPERATURE,

    val hourlyForecastSectionState: HourlyForecastSectionState = HourlyForecastSectionState(),
    val dailyForecastSectionState: DailyForecastSectionState = DailyForecastSectionState(),
) {

    @Immutable
    data class HourlyForecastSectionState(
        val forecast: FetchedData<HourlyForecast> = FetchedData(),
        val moreAllowed: Boolean = true,
    )

    @Immutable
    data class DailyForecastSectionState(
        val forecast: FetchedData<DailyForecast> = FetchedData(),
    )
}