package cszsm.dolgok.forecast.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cszsm.dolgok.core.presentation.asLocalizedDayOfWeek
import cszsm.dolgok.core.presentation.asPressure
import cszsm.dolgok.core.presentation.asRain
import cszsm.dolgok.core.presentation.asTemperature
import cszsm.dolgok.core.presentation.components.sectionheader.SectionHeader
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItem
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItemShapeParams
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecastUnit
import cszsm.dolgok.forecast.presentation.WeatherVariable
import cszsm.dolgok.localization.R
import kotlinx.datetime.LocalTime

private val FIRST_HOUR_OF_THE_DAY = LocalTime(hour = 0, minute = 0)
private val LAST_HOUR_OF_THE_DAY = LocalTime(hour = 23, minute = 0)

private const val KEY_DAY_TODAY = "today"

@Composable
internal fun HourlyForecastList(
    forecast: HourlyForecast,
    loading: Boolean,
    selectedWeatherVariable: WeatherVariable,
    onEndReach: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState.canScrollForward) {
        if (!listState.canScrollForward) {
            onEndReach()
        }
    }

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = modifier,
    ) {
        item(key = KEY_DAY_TODAY) {
            SectionHeader(text = stringResource(R.string.forecast_today))
        }

        forecast.hours.forEachIndexed { index, forecastUnit ->
            val time = forecastUnit.time.time.toString()
            val forecastValue = forecastUnit.getLabel(weatherVariable = selectedWeatherVariable)

            if (forecastUnit.time.time == LocalTime(hour = 0, minute = 0)) {
                val day = forecastUnit.time.asLocalizedDayOfWeek().lowercase()
                item(key = day) {
                    SectionHeader(text = day)
                }
            }

            item(key = forecastUnit.time.toString()) {
                SingleValueListItem(
                    title = time,
                    value = forecastValue,
                    shapeParams = SingleValueListItemShapeParams(
                        index = index, size = forecast.hours.size,
                        forcedTop = forecastUnit.time.time == FIRST_HOUR_OF_THE_DAY,
                        forcedBottom = forecastUnit.time.time == LAST_HOUR_OF_THE_DAY,
                    )
                )
            }
        }

        if (loading) {
            item {
                Text(text = stringResource(R.string.core_loading))
            }
        }
    }
}

private fun HourlyForecastUnit.getLabel(
    weatherVariable: WeatherVariable,
) = when (weatherVariable) {
    WeatherVariable.TEMPERATURE -> temperature.asTemperature()
    WeatherVariable.RAIN -> rain.asRain()
    WeatherVariable.PRESSURE -> pressure.asPressure()
}
