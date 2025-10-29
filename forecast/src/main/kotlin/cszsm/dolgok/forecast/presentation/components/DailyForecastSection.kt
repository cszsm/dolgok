package cszsm.dolgok.forecast.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cszsm.dolgok.core.presentation.components.error.FullScreenError
import cszsm.dolgok.core.presentation.components.loading.FullScreenLoading
import cszsm.dolgok.core.presentation.components.sectionheader.SectionHeader
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItem
import cszsm.dolgok.core.presentation.components.singlevaluelistitem.SingleValueListItemShapeParams
import cszsm.dolgok.core.presentation.displayName
import cszsm.dolgok.core.presentation.error.getMessage
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.presentation.ForecastScreenState
import cszsm.dolgok.forecast.presentation.WeatherVariable
import cszsm.dolgok.localization.R
import kotlinx.datetime.DayOfWeek

private const val KEY_NEXT_WEEK = "next_week"

@Composable
internal fun DailyForecastSection(
    state: ForecastScreenState.DailyForecastSectionState,
    selectedWeatherVariable: WeatherVariable,
) {
    if (state.forecast.data == null) {
        when {
            state.forecast.loading -> FullScreenLoading()
            state.forecast.error != null -> FullScreenError(message = state.forecast.error!!.getMessage())
        }
    } else {
        DailyForecastList(
            forecast = state.forecast.data!!,
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

            val forecastValue = variables.getLabel(
                units = forecast.units,
                weatherVariable = selectedWeatherVariable,
            )

            if (index != 0 && date.dayOfWeek == DayOfWeek.MONDAY) {
                item(key = KEY_NEXT_WEEK) {
                    SectionHeader(
                        text = stringResource(R.string.forecast_next_week),
                        modifier = Modifier.animateItem(),
                    )
                }
            }

            item(key = date.toString()) {
                SingleValueListItem(
                    title = date.dayOfWeek.displayName().lowercase(),
                    value = forecastValue,
                    shapeParams = SingleValueListItemShapeParams(
                        index = index,
                        size = forecast.days.size,
                        forcedTop = date.dayOfWeek == DayOfWeek.MONDAY,
                        forcedBottom = date.dayOfWeek == DayOfWeek.SUNDAY,
                    )
                )
            }
        }
    }
}

private fun DailyForecast.Variables.getLabel(
    units: DailyForecast.Units,
    weatherVariable: WeatherVariable,
) = when (weatherVariable) {
    WeatherVariable.TEMPERATURE ->
        "${temperatureMin.asLabel(units.temperatureMin)} - ${temperatureMax.asLabel(units.temperatureMax)}"

    WeatherVariable.RAIN ->
        rainSum.asLabel(units.rainSum)

    WeatherVariable.PRESSURE -> "" // no pressure data for daily forecast
}

private fun Float.asLabel(unit: String) = "${"%.2f".format(this)} $unit"