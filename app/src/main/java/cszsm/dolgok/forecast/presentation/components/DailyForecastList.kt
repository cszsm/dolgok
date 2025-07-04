package cszsm.dolgok.forecast.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cszsm.dolgok.core.presentation.asRain
import cszsm.dolgok.core.presentation.asTemperature
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItem
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItemShapeParams
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.DailyForecastUnit
import cszsm.dolgok.forecast.presentation.WeatherVariable

@Composable
fun DailyForecastList(
    forecast: DailyForecast,
    selectedWeatherVariable: WeatherVariable,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = modifier,
    ) {
        forecast.days.forEachIndexed { index, forecastUnit ->
            val day = forecastUnit.date.dayOfWeek.toString().lowercase()
            val forecastValue = forecastUnit.getLabel(weatherVariable = selectedWeatherVariable)

            item(key = forecastUnit.date.toString()) {
                SingleValueListItem(
                    title = day,
                    value = forecastValue,
                    shapeParams = SingleValueListItemShapeParams(
                        index = index,
                        size = forecast.days.size
                    )
                )
            }
        }
    }
}

private fun DailyForecastUnit.getLabel(
    weatherVariable: WeatherVariable,
) = when (weatherVariable) {
    WeatherVariable.TEMPERATURE -> "${temperatureMin.asTemperature()} - ${temperatureMax.asTemperature()}"
    WeatherVariable.RAIN -> rainSum.asRain()
    WeatherVariable.PRESSURE -> "" // no pressure data for daily forecast
}
