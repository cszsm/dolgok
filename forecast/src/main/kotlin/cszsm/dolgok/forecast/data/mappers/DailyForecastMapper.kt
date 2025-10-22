package cszsm.dolgok.forecast.data.mappers

import cszsm.dolgok.forecast.data.models.DailyForecastApiModel
import cszsm.dolgok.forecast.domain.models.DailyForecast
import kotlinx.datetime.LocalDate

internal class DailyForecastMapper {

    fun map(input: DailyForecastApiModel) = DailyForecast(
        days = input.daily.map(),
        units = input.daily_units.map(),
    )

    private fun DailyForecastApiModel.Variables.map() =
        buildMap {
            time.forEachIndexed { index, time ->
                val parsedTime = LocalDate.parse(time)
                val forecastVariables = DailyForecast.Variables(
                    temperatureMax = temperature_2m_max[index],
                    temperatureMin = temperature_2m_min[index],
                    rainSum = rain_sum[index],
                )
                put(parsedTime, forecastVariables)
            }
        }

    private fun DailyForecastApiModel.Units.map() =
        DailyForecast.Units(
            temperatureMax = temperature_2m_max,
            temperatureMin = temperature_2m_min,
            rainSum = rain_sum,
        )
}