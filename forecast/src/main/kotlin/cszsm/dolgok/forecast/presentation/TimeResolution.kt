package cszsm.dolgok.forecast.presentation

internal enum class TimeResolution {
    HOURLY,
    DAILY;

    val weatherVariables
        get() = when (this) {
            HOURLY -> listOf(
                WeatherVariable.TEMPERATURE,
                WeatherVariable.RAIN,
                WeatherVariable.PRESSURE
            )

            DAILY -> listOf(
                WeatherVariable.TEMPERATURE,
                WeatherVariable.RAIN,
            )
        }
}