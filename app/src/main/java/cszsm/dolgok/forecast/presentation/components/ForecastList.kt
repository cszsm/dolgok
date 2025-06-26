package cszsm.dolgok.forecast.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cszsm.dolgok.core.presentation.components.sectionheader.SectionHeader
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItem
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItemShapeParams
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.DailyForecastUnit
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecastUnit
import cszsm.dolgok.forecast.presentation.WeatherVariable
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

private val FIRST_HOUR_OF_THE_DAY = LocalTime(hour = 0, minute = 0)
private val LAST_HOUR_OF_THE_DAY = LocalTime(hour = 23, minute = 0)

@Composable
fun HourlyForecastList(
    forecast: HourlyForecast,
    selectedWeatherVariable: WeatherVariable,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
    ) {
        val currentDayOfWeek = forecast.hours?.firstOrNull()?.time?.dayOfWeek
            ?: return@LazyColumn

        item {
            SectionHeader(text = "today")
        }

        forecast.hours.forEachIndexed { index, forecastUnit ->
            forecastUnit.time ?: return@forEachIndexed

            val time = forecastUnit.time.time.toString()
            val day = forecastUnit.getDayLabel(currentDayOfWeek = currentDayOfWeek)
            val forecastValue = forecastUnit.getLabel(weatherVariable = selectedWeatherVariable)

            if (forecastUnit.time.time == LocalTime(hour = 0, minute = 0)) {
                item {
                    SectionHeader(text = day)
                }
            }

            item {
                SingleValueListItem(
                    title = time,
                    value = forecastValue ?: "",
                    shapeParams = SingleValueListItemShapeParams(
                        index = index, size = forecast.hours.size,
                        forcedTop = forecastUnit.time.time == FIRST_HOUR_OF_THE_DAY,
                        forcedBottom = forecastUnit.time.time == LAST_HOUR_OF_THE_DAY,
                    )
                )
            }
        }
    }
}

@Composable
fun DailyForecastList(
    forecast: DailyForecast,
    selectedWeatherVariable: WeatherVariable,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
    ) {
        forecast.days?.forEachIndexed { index, forecastUnit ->
            val day = forecastUnit.time?.dayOfWeek?.toString()?.lowercase() ?: ""
            val forecastValue = forecastUnit.getLabel(weatherVariable = selectedWeatherVariable)

            item {
                SingleValueListItem(
                    title = day,
                    value = forecastValue ?: "",
                    shapeParams = SingleValueListItemShapeParams(
                        index = index,
                        size = forecast.days.size
                    )
                )
            }
        }
    }
}

private fun HourlyForecastUnit.getDayLabel(
    currentDayOfWeek: DayOfWeek,
): String {
    val forecastDayOfWeek = time?.dayOfWeek

    return if (forecastDayOfWeek != currentDayOfWeek) {
        forecastDayOfWeek.toString().lowercase()
    } else {
        ""
    }
}

private fun HourlyForecastUnit.getLabel(
    weatherVariable: WeatherVariable,
) = when (weatherVariable) {
    WeatherVariable.TEMPERATURE -> temperature?.asTemperature()
    WeatherVariable.RAIN -> rain?.asRain()
    WeatherVariable.PRESSURE -> pressure?.asPressure()
}

private fun DailyForecastUnit.getLabel(
    weatherVariable: WeatherVariable,
) = when (weatherVariable) {
    WeatherVariable.TEMPERATURE -> "${temperatureMin?.asTemperature()} - ${temperatureMax?.asTemperature()}"
    WeatherVariable.RAIN -> rainSum?.asRain()
    WeatherVariable.PRESSURE -> "" // no pressure data for daily forecast
}


private fun Float.asTemperature() = "$this ${Typography.degree}C"

private fun Float.asRain() = "$this mm"

private fun Float.asPressure() = "$this hPa"