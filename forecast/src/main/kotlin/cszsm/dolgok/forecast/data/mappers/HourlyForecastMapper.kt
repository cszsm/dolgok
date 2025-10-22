package cszsm.dolgok.forecast.data.mappers

import cszsm.dolgok.forecast.data.models.HourlyForecastApiModel
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import kotlinx.datetime.LocalDateTime

internal class HourlyForecastMapper {

    fun map(input: HourlyForecastApiModel) =
        HourlyForecast(
            hours = input.hourly.map(),
            units = input.hourly_units.map(),
        )


    private fun HourlyForecastApiModel.Variables.map() =
        buildMap {
            time.forEachIndexed { index, time ->
                val parsedTime = LocalDateTime.parse(time)
                val forecastVariables = HourlyForecast.Variables(
                    temperature = temperature_2m[index],
                    rain = rain[index],
                    pressure = surface_pressure[index],
                )
                put(parsedTime, forecastVariables)
            }
        }

    private fun HourlyForecastApiModel.Units.map() =
        HourlyForecast.Units(
            temperature = temperature_2m,
            rain = rain,
            pressure = surface_pressure,
        )
}