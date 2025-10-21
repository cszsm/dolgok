package cszsm.dolgok.forecast.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cszsm.dolgok.core.domain.models.FetchedData
import cszsm.dolgok.core.presentation.asLocalizedDayOfWeek
import cszsm.dolgok.core.presentation.asRain
import cszsm.dolgok.core.presentation.asTemperature
import cszsm.dolgok.core.presentation.components.error.FullScreenError
import cszsm.dolgok.core.presentation.components.loading.FullScreenLoading
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItem
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItemShapeParams
import cszsm.dolgok.core.presentation.error.getMessage
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.presentation.WeatherVariable

@Composable
internal fun DailyForecastSection(
    state: FetchedData<DailyForecast>,
    selectedWeatherVariable: WeatherVariable,
) {
    if (state.data == null) {
        when {
            state.loading -> FullScreenLoading()
            state.error != null -> FullScreenError(message = state.error!!.getMessage())
        }
    } else {
        DailyForecastList(
            forecast = state.data!!,
            selectedWeatherVariable = selectedWeatherVariable,
        )
    }
}

@Composable
private fun DailyForecastList(
    forecast: DailyForecast,
    selectedWeatherVariable: WeatherVariable,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxHeight(),
    ) {
        forecast.days.entries.forEachIndexed { index, day ->
            val date = day.key
            val variables = day.value

            val dayOfWeek = date.asLocalizedDayOfWeek().lowercase()
            val forecastValue = variables.getLabel(weatherVariable = selectedWeatherVariable)

            item(key = date.toString()) {
                SingleValueListItem(
                    title = dayOfWeek,
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

private fun DailyForecast.Variables.getLabel(
    weatherVariable: WeatherVariable,
) = when (weatherVariable) {
    WeatherVariable.TEMPERATURE -> "${temperatureMin.asTemperature()} - ${temperatureMax.asTemperature()}"
    WeatherVariable.RAIN -> rainSum.asRain()
    WeatherVariable.PRESSURE -> "" // no pressure data for daily forecast
}
