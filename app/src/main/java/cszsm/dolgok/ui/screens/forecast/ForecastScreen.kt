package cszsm.dolgok.ui.screens.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
        color = MaterialTheme.colorScheme.background,
    ) {
        ForecastContent(
            forecast = state.forecast,
        )
    }
}

@Composable
private fun ForecastContent(
    forecast: Forecast?,
) {
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
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(12.dp),
        ) {
            val currentDayOfWeek = forecast.hourly.firstOrNull()?.time?.dayOfWeek
                ?: return@LazyColumn

            forecast.hourly.forEach {
                val time = it.time?.time.toString()
                val day = it.getDayLabel(currentDayOfWeek = currentDayOfWeek)
                val temperature = it.temperature.toString()
                item {
                    TemperatureItem(
                        time = time,
                        day = day,
                        temperature = temperature,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TemperatureItem(
    time: String = "11:00",
    day: String = "monday",
    temperature: String = "10.1",
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
                style = Typography.headlineSmall,
                modifier = Modifier
                    .padding(12.dp)
            )
            Text(
                text = day,
                style = Typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(12.dp)
            )
            Card(
                modifier = Modifier
                    .width(140.dp)
            ) {
                Text(
                    text = "$temperature ${kotlin.text.Typography.degree}C",
                    style = Typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ForecastContent_Preview() {
    val forecast = Forecast(
        hourly = listOf(
            ForecastUnit(time = getPreviewTime(dayOfMonth = 22, hour = 22), temperature = 11.4f),
            ForecastUnit(time = getPreviewTime(dayOfMonth = 22, hour = 23), temperature = 11.7f),
            ForecastUnit(time = getPreviewTime(dayOfMonth = 23, hour = 0), temperature = 10.2f),
            ForecastUnit(time = getPreviewTime(dayOfMonth = 23, hour = 1), temperature = 9.9f),
        )
    )
    ForecastContent(forecast = forecast)
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