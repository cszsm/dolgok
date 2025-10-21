package cszsm.dolgok.forecast.data.mappers

import cszsm.dolgok.forecast.data.models.HourlyForecastApiModel
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import kotlinx.datetime.LocalDateTime

internal class HourlyForecastMapper {

    fun map(input: HourlyForecastApiModel): HourlyForecast {
        return input.hourly.map()
    }

    private fun HourlyForecastApiModel.Variables.map(): HourlyForecast {
        val hours = buildMap {
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
        return HourlyForecast(hours = hours)
    }
}