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
            forecast.hourly.forEach {
                val time = it.time?.time.toString()
                val temperature = it.temperature.toString()
                item {
                    TemperatureItem(
                        time = time,
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
            ForecastUnit(time = getHour(hour = 8), temperature = 11f),
            ForecastUnit(time = getHour(hour = 9), temperature = 12f),
            ForecastUnit(time = getHour(hour = 10), temperature = 14f),
            ForecastUnit(time = getHour(hour = 11), temperature = 17f),
        )
    )
    ForecastContent(forecast = forecast)
}

private fun getHour(hour: Int) =
    LocalDateTime(year = 2025, monthNumber = 3, dayOfMonth = 22, hour = hour, minute = 0)