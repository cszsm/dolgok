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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cszsm.dolgok.domain.dto.Forecast
import cszsm.dolgok.domain.dto.ForecastUnit
import cszsm.dolgok.ui.theme.Typography
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    viewModel.loadForecast()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        ForecastContent(
            forecast = state.forecast,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForecastContent(
    forecast: Forecast?,
) {
    var selectedWeatherVariable by remember { mutableStateOf<WeatherVariable>(WeatherVariable.TEMPERATURE) }

    Column {
        TopAppBar(
            title = {
                Text(text = "Forecast")
            }
        )
        if (forecast == null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Loading...",
                    style = Typography.headlineMedium,
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                WeatherVariableSelector(
                    selected = selectedWeatherVariable,
                    onSelect = { selectedWeatherVariable = it }
                )
                ForecastList(
                    forecast = forecast,
                    selectedWeatherVariable = selectedWeatherVariable,
                )
            }
        }
    }
}

@Composable
private fun WeatherVariableSelector(
    selected: WeatherVariable,
    onSelect: (WeatherVariable) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        WeatherVariable.entries.forEachIndexed { index, weatherVariable ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = WeatherVariable.entries.size
                ),
                label = { Text(weatherVariable.label) },
                selected = weatherVariable == selected,
                onClick = { onSelect(weatherVariable) }
            )
        }
    }
}

@Composable
private fun ForecastList(
    forecast: Forecast,
    selectedWeatherVariable: WeatherVariable,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(bottom = 12.dp),
    ) {
        val currentDayOfWeek = forecast.hourly.firstOrNull()?.time?.dayOfWeek
            ?: return@LazyColumn

        forecast.hourly.forEach {
            val time = it.time?.time.toString()
            val day = it.getDayLabel(currentDayOfWeek = currentDayOfWeek)
            val forecastValue = it.getValue(weatherVariable = selectedWeatherVariable)
            item {
                ForecastItem(
                    time = time,
                    day = day,
                    forecastValue = forecastValue ?: "",
                )
            }
        }
    }
}

@Composable
private fun ForecastItem(
    time: String,
    day: String,
    forecastValue: String,
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
                text = time,
                style = Typography.titleMedium,
                modifier = Modifier
                    .padding(12.dp)
            )
            Text(
                text = day,
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(12.dp)
            )
            Card(
                modifier = Modifier
                    .width(140.dp)
            ) {
                Text(
                    text = forecastValue,
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

private val WeatherVariable.label
    get() = when (this) {
        WeatherVariable.TEMPERATURE -> "Temperature"
        WeatherVariable.RAIN -> "Rain"
        WeatherVariable.PRESSURE -> "Pressure"
    }

private fun Float.asTemperature() = "$this ${kotlin.text.Typography.degree}C"

private fun Float.asRain() = "$this mm"

private fun Float.asPressure() = "$this hPa"

private fun ForecastUnit.getValue(
    weatherVariable: WeatherVariable,
) = when (weatherVariable) {
    WeatherVariable.TEMPERATURE -> temperature?.asTemperature()
    WeatherVariable.RAIN -> rain?.asRain()
    WeatherVariable.PRESSURE -> pressure?.asPressure()
}

private fun getPreviewTime(dayOfMonth: Int, hour: Int) =
    LocalDateTime(year = 2025, monthNumber = 3, dayOfMonth = dayOfMonth, hour = hour, minute = 0)

private fun ForecastUnit.getDayLabel(
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
        time = "11:00",
        day = "monday",
        forecastValue = 10.1f.asTemperature(),
    )
}

@Preview
@Composable
private fun ForecastContent_Preview() {
    val forecast = Forecast(
        hourly = listOf(
            ForecastUnit(
                time = getPreviewTime(dayOfMonth = 22, hour = 22),
                temperature = 11.4f,
                rain = 0f,
                pressure = 999.7f
            ),
            ForecastUnit(
                time = getPreviewTime(dayOfMonth = 22, hour = 23),
                temperature = 11.7f,
                rain = 0f,
                pressure = 998.6f
            ),
            ForecastUnit(
                time = getPreviewTime(dayOfMonth = 23, hour = 0),
                temperature = 10.2f,
                rain = 0.3f,
                pressure = 997f
            ),
            ForecastUnit(
                time = getPreviewTime(dayOfMonth = 23, hour = 1),
                temperature = 9.9f,
                rain = 0.1f,
                pressure = 996.4f
            ),
        )
    )
    ForecastContent(forecast = forecast)
}
