package cszsm.dolgok.forecast.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cszsm.dolgok.core.domain.models.FetchedData
import cszsm.dolgok.core.presentation.asLocalizedDayOfWeek
import cszsm.dolgok.core.presentation.asPressure
import cszsm.dolgok.core.presentation.asRain
import cszsm.dolgok.core.presentation.asTemperature
import cszsm.dolgok.core.presentation.components.error.FullScreenError
import cszsm.dolgok.core.presentation.components.loading.FullScreenLoading
import cszsm.dolgok.core.presentation.components.sectionheader.SectionHeader
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItem
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItemShapeParams
import cszsm.dolgok.core.presentation.error.getMessage
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.presentation.WeatherVariable
import cszsm.dolgok.localization.R
import kotlinx.datetime.LocalTime

private val FIRST_HOUR_OF_THE_DAY = LocalTime(hour = 0, minute = 0)
private val LAST_HOUR_OF_THE_DAY = LocalTime(hour = 23, minute = 0)

private const val KEY_DAY_TODAY = "today"

@Composable
internal fun HourlyForecastSection(
    state: FetchedData<HourlyForecast>,
    selectedWeatherVariable: WeatherVariable,
    onEndReach: () -> Unit,
) {
    if (state.data == null) {
        when {
            state.loading -> FullScreenLoading()
            state.error != null -> FullScreenError(message = state.error!!.getMessage())
        }
    } else {
        HourlyForecastList(
            state = state,
            selectedWeatherVariable = selectedWeatherVariable,
            onEndReach = onEndReach,
        )
    }
}

@Composable
private fun HourlyForecastList(
    state: FetchedData<HourlyForecast>,
    selectedWeatherVariable: WeatherVariable,
    onEndReach: () -> Unit,
) {
    val forecast = state.data ?: return
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
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
    ) {
        item(key = KEY_DAY_TODAY) {
            SectionHeader(
                text = stringResource(R.string.forecast_today),
                modifier = Modifier.animateItem(),
            )
        }

        forecast.hours.entries.forEachIndexed { index, entry ->
            val dateTime = entry.key
            val time = dateTime.time
            val variables = entry.value
            val forecastValue = variables.getLabel(weatherVariable = selectedWeatherVariable)

            if (time == LocalTime(hour = 0, minute = 0)) {
                val day = entry.key.asLocalizedDayOfWeek().lowercase()
                item(key = day) {
                    SectionHeader(
                        text = day,
                        modifier = Modifier.animateItem(),
                    )
                }
            }

            item(key = dateTime.toString()) {
                SingleValueListItem(
                    title = time.toString(),
                    value = forecastValue,
                    shapeParams = SingleValueListItemShapeParams(
                        index = index, size = forecast.hours.size,
                        forcedTop = time == FIRST_HOUR_OF_THE_DAY,
                        forcedBottom = time == LAST_HOUR_OF_THE_DAY,
                    ),
                    modifier = Modifier.animateItem(),
                )
            }
        }

        when {
            state.loading -> item { Text(text = stringResource(R.string.core_loading)) }
            state.error != null -> item { Text(text = state.error!!.getMessage()) }
        }
    }
}

private fun HourlyForecast.Variables.getLabel(
    weatherVariable: WeatherVariable,
) = when (weatherVariable) {
    WeatherVariable.TEMPERATURE -> temperature.asTemperature()
    WeatherVariable.RAIN -> rain.asRain()
    WeatherVariable.PRESSURE -> pressure.asPressure()
}
