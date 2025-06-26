package cszsm.dolgok.forecast.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.DailyForecastUnit
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecastUnit
import cszsm.dolgok.core.presentation.theme.Typography
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        ForecastContent(
            hourlyForecast = state.hourlyForecast,
            dailyForecast = state.dailyForecast,
            selectedTimeResolution = state.selectedTimeResolution,
            selectedWeatherVariable = state.selectedWeatherVariable,
            onTimeResolutionSelect = viewModel::onTimeResolutionChange,
            onWeatherVariableChange = viewModel::onWeatherVariableChange,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForecastContent(
    hourlyForecast: HourlyForecast?,
    dailyForecast: DailyForecast?,
    selectedTimeResolution: TimeResolution,
    selectedWeatherVariable: WeatherVariable,
    onTimeResolutionSelect: (TimeResolution) -> Unit,
    onWeatherVariableChange: (WeatherVariable) -> Unit,
) {
    Column {
        TopAppBar(
            title = {
                Text(text = "Forecast")
            }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            TimeResolutionSelector(
                selectedResolution = selectedTimeResolution,
                onSelect = onTimeResolutionSelect
            )

            when (selectedTimeResolution) {
                TimeResolution.HOURLY ->
                    if (hourlyForecast == null) {
                        Loading()
                    } else {
                        WeatherVariableSelector(
                            weatherVariables = cszsm.dolgok.forecast.presentation.TimeResolution.HOURLY.weatherVariables,
                            selectedWeatherVariable = selectedWeatherVariable,
                            onSelect = onWeatherVariableChange,
                        )
                        HourlyForecastList(
                            forecast = hourlyForecast,
                            selectedWeatherVariable = selectedWeatherVariable,
                        )
                    }

                TimeResolution.DAILY ->
                    if (dailyForecast == null) {
                        Loading()
                    } else {
                        WeatherVariableSelector(
                            weatherVariables = cszsm.dolgok.forecast.presentation.TimeResolution.DAILY.weatherVariables,
                            selectedWeatherVariable = selectedWeatherVariable,
                            onSelect = onWeatherVariableChange,
                        )
                        DailyForecastList(
                            forecast = dailyForecast,
                            selectedWeatherVariable = selectedWeatherVariable,
                        )
                    }
            }
        }
    }
}

@Composable
private fun Loading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Loading...",
            style = Typography.headlineMedium,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TimeResolutionSelector(
    selectedResolution: TimeResolution,
    onSelect: (TimeResolution) -> Unit,
) {
    val resolutions = TimeResolution.entries

    Row(
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        resolutions.forEachIndexed { index, resolution ->
            ToggleButton(
                modifier = Modifier.weight(1f),
                shapes = getToggleButtonShape(
                    currentIndex = index,
                    lastIndex = resolutions.lastIndex
                ),
                checked = selectedResolution == resolution,
                onCheckedChange = { onSelect(resolution) },
            ) {
                Text(text = resolution.label)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun WeatherVariableSelector(
    weatherVariables: List<WeatherVariable>,
    selectedWeatherVariable: WeatherVariable,
    onSelect: (WeatherVariable) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)) {
        weatherVariables.forEachIndexed { index, variable ->
            ToggleButton(
                modifier = Modifier.weight(1f),
                shapes = getToggleButtonShape(
                    currentIndex = index,
                    lastIndex = weatherVariables.lastIndex
                ),
                checked = selectedWeatherVariable == variable,
                onCheckedChange = { onSelect(variable) }
            ) {
                Text(text = variable.label)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun getToggleButtonShape(
    currentIndex: Int,
    lastIndex: Int,
) = when (currentIndex) {
    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
    lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
}

@Composable
private fun HourlyForecastList(
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
            Text(
                text = "today",
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        forecast.hours.forEachIndexed { index, forecastUnit ->
            forecastUnit.time ?: return@forEachIndexed

            val time = forecastUnit.time.time.toString()
            val day = forecastUnit.getDayLabel(currentDayOfWeek = currentDayOfWeek)
            val forecastValue = forecastUnit.getValue(weatherVariable = selectedWeatherVariable)

            if (forecastUnit.time.time == LocalTime(hour = 0, minute = 0)) {
                item {
                    Text(
                        text = day,
                        style = Typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            item {
                ForecastItem(
                    shapes = getForecastItemShape(
                        index = index,
                        size = forecast.hours.size,
                        forcedTop = forecastUnit.time.time == FIRST_HOUR_OF_THE_DAY,
                        forcedBottom = forecastUnit.time.time == LAST_HOUR_OF_THE_DAY,
                    ),
                    primaryLabel = time,
                    valueLabel = forecastValue ?: "",
                )
            }
        }
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
    ) {
        forecast.days?.forEachIndexed { index, forecastUnit ->
            val day = forecastUnit.time?.dayOfWeek?.toString()?.lowercase() ?: ""
            val forecastValue = forecastUnit.getLabel(weatherVariable = selectedWeatherVariable)

            item {
                ForecastItem(
                    shapes = getForecastItemShape(index = index, size = forecast.days.size),
                    primaryLabel = day,
                    valueLabel = forecastValue ?: "",
                )
            }
        }
    }
}

@Composable
private fun ForecastItem(
    shapes: ForecastItemShapes,
    primaryLabel: String,
    valueLabel: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Surface(
            shape = shapes.leadingShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.weight(6f)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = primaryLabel,
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
        Surface(
            shape = shapes.trailingShape,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.weight(4f),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = valueLabel,
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

private val TimeResolution.label
    get() = when (this) {
        TimeResolution.HOURLY -> "Hourly"
        TimeResolution.DAILY -> "Daily"
    }

private val TimeResolution.weatherVariables
    get() = when (this) {
        TimeResolution.HOURLY -> listOf(
            WeatherVariable.TEMPERATURE,
            WeatherVariable.RAIN,
            WeatherVariable.PRESSURE
        )

        TimeResolution.DAILY -> listOf(
            WeatherVariable.TEMPERATURE,
            WeatherVariable.RAIN,
        )
    }

private val WeatherVariable.label
    get() = when (this) {
        WeatherVariable.TEMPERATURE -> "Temperature"
        WeatherVariable.RAIN -> "Rain"
        WeatherVariable.PRESSURE -> "Pressure"
    }

private fun Float.asTemperature() = "$this ${kotlin.text.Typography.degree}C"

private fun Float.asRain() = "$this mm"

private fun Float.asPressure() = "$this hPa"

private fun HourlyForecastUnit.getValue(
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

private fun getPreviewTime(dayOfMonth: Int, hour: Int) =
    LocalDateTime(year = 2025, monthNumber = 3, dayOfMonth = dayOfMonth, hour = hour, minute = 0)

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

private sealed interface ForecastItemShapes {
    val leadingShape: Shape
    val trailingShape: Shape

    data object Single : ForecastItemShapes {
        override val leadingShape = RoundedCornerShape(
            topStart = RADIUS_LARGE,
            bottomStart = RADIUS_LARGE,
            topEnd = RADIUS_SMALL,
            bottomEnd = RADIUS_SMALL,
        )
        override val trailingShape = RoundedCornerShape(
            topStart = RADIUS_SMALL,
            bottomStart = RADIUS_SMALL,
            topEnd = RADIUS_LARGE,
            bottomEnd = RADIUS_LARGE,
        )
    }

    data object Top : ForecastItemShapes {
        override val leadingShape = RoundedCornerShape(
            topStart = RADIUS_LARGE,
            bottomStart = RADIUS_SMALL,
            topEnd = RADIUS_SMALL,
            bottomEnd = RADIUS_SMALL,
        )
        override val trailingShape = RoundedCornerShape(
            topStart = RADIUS_SMALL,
            bottomStart = RADIUS_SMALL,
            topEnd = RADIUS_LARGE,
            bottomEnd = RADIUS_SMALL,
        )
    }

    data object Middle : ForecastItemShapes {
        override val leadingShape = RoundedCornerShape(
            size = RADIUS_SMALL,
        )
        override val trailingShape = RoundedCornerShape(
            size = RADIUS_SMALL,
        )
    }

    data object Bottom : ForecastItemShapes {
        override val leadingShape = RoundedCornerShape(
            topStart = RADIUS_SMALL,
            bottomStart = RADIUS_LARGE,
            topEnd = RADIUS_SMALL,
            bottomEnd = RADIUS_SMALL,
        )
        override val trailingShape = RoundedCornerShape(
            topStart = RADIUS_SMALL,
            bottomStart = RADIUS_SMALL,
            topEnd = RADIUS_SMALL,
            bottomEnd = RADIUS_LARGE,
        )
    }

    private companion object {
        val RADIUS_SMALL = 4.dp
        val RADIUS_LARGE = 16.dp
    }
}

private fun getForecastItemShape(
    index: Int,
    size: Int,
    forcedTop: Boolean = false,
    forcedBottom: Boolean = false,
) = when {
    size == 1 -> ForecastItemShapes.Single
    forcedTop || index == 0 -> ForecastItemShapes.Top
    forcedBottom || index == size - 1 -> ForecastItemShapes.Bottom
    else -> ForecastItemShapes.Middle
}


private val FIRST_HOUR_OF_THE_DAY = LocalTime(hour = 0, minute = 0)
private val LAST_HOUR_OF_THE_DAY = LocalTime(hour = 23, minute = 0)

@Preview
@Composable
private fun ForecastItem_Preview() {
    ForecastItem(
        shapes = ForecastItemShapes.Single,
        primaryLabel = "11:00",
        valueLabel = 10.1f.asTemperature(),
    )
}

@Preview
@Composable
private fun ForecastContent_Preview() {
    val hourlyForecast = HourlyForecast(
        hours = listOf(
            HourlyForecastUnit(
                time = getPreviewTime(dayOfMonth = 22, hour = 22),
                temperature = 11.4f,
                rain = 0f,
                pressure = 999.7f
            ),
            HourlyForecastUnit(
                time = getPreviewTime(dayOfMonth = 22, hour = 23),
                temperature = 11.7f,
                rain = 0f,
                pressure = 998.6f
            ),
            HourlyForecastUnit(
                time = getPreviewTime(dayOfMonth = 23, hour = 0),
                temperature = 10.2f,
                rain = 0.3f,
                pressure = 997f
            ),
            HourlyForecastUnit(
                time = getPreviewTime(dayOfMonth = 23, hour = 1),
                temperature = 9.9f,
                rain = 0.1f,
                pressure = 996.4f
            ),
        )
    )
    ForecastContent(
        hourlyForecast = hourlyForecast,
        dailyForecast = null,
        selectedTimeResolution = TimeResolution.HOURLY,
        selectedWeatherVariable = WeatherVariable.TEMPERATURE,
        onTimeResolutionSelect = {},
        onWeatherVariableChange = {},
    )
}
