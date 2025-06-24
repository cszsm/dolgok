package cszsm.dolgok.ui.screens.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cszsm.dolgok.domain.dto.DailyForecast
import cszsm.dolgok.domain.dto.DailyForecastUnit
import cszsm.dolgok.domain.dto.HourlyForecast
import cszsm.dolgok.domain.dto.HourlyForecastUnit
import cszsm.dolgok.ui.theme.Typography
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
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
                            weatherVariables = TimeResolution.HOURLY.weatherVariables,
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
                            weatherVariables = TimeResolution.DAILY.weatherVariables,
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
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    resolutions.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                },
                checked = selectedResolution == resolution,
                onCheckedChange = { onSelect(resolution) },
            ) {
                Text(text = resolution.label)
            }
        }
    }
}

@Composable
private fun WeatherVariableSelector(
    weatherVariables: List<WeatherVariable>,
    selectedWeatherVariable: WeatherVariable,
    onSelect: (WeatherVariable) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        weatherVariables.forEachIndexed { index, weatherVariable ->
            SegmentedButton(
                modifier = Modifier.weight(1f),
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = weatherVariables.size
                ),
                label = { Text(weatherVariable.label) },
                selected = weatherVariable == selectedWeatherVariable,
                onClick = { onSelect(weatherVariable) }
            )
        }
    }
}

@Composable
private fun HourlyForecastList(
    forecast: HourlyForecast,
    selectedWeatherVariable: WeatherVariable,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(bottom = 12.dp),
    ) {
        val currentDayOfWeek = forecast.hours?.firstOrNull()?.time?.dayOfWeek
            ?: return@LazyColumn

        forecast.hours.forEach {
            val time = it.time?.time?.toString() ?: ""
            val day = it.getDayLabel(currentDayOfWeek = currentDayOfWeek)
            val forecastValue = it.getValue(weatherVariable = selectedWeatherVariable)
            item {
                ForecastItem(
                    primaryLabel = time,
                    secondaryLabel = day,
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
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(bottom = 12.dp),
    ) {
        forecast.days?.forEach {
            val day = it.time?.dayOfWeek?.toString()?.lowercase() ?: ""
            val forecastValue = it.getLabel(weatherVariable = selectedWeatherVariable)

            item {
                ForecastItem(
                    primaryLabel = day,
                    valueLabel = forecastValue ?: "",
                )
            }
        }
    }
}

@Composable
private fun ForecastItem(
    primaryLabel: String,
    secondaryLabel: String = "",
    valueLabel: String,
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = primaryLabel,
                style = Typography.titleMedium,
                modifier = Modifier
                    .padding(12.dp)
            )
            Text(
                text = secondaryLabel,
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(12.dp)
            )
            Card(
                modifier = Modifier
                    .width(180.dp)
            ) {
                Text(
                    text = valueLabel,
                    style = Typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
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

@Preview
@Composable
private fun ForecastItem_Preview() {
    ForecastItem(
        primaryLabel = "11:00",
        secondaryLabel = "monday",
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
