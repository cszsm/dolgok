package cszsm.dolgok.forecast.presentation

internal sealed interface ForecastScreenEvent {
    data class TimeResolutionChange(val timeResolution: TimeResolution) : ForecastScreenEvent
    data class WeatherVariableChange(val weatherVariable: WeatherVariable) : ForecastScreenEvent
    data object HourlyForecastEndReach : ForecastScreenEvent
}