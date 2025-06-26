package cszsm.dolgok.forecast.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cszsm.dolgok.core.presentation.components.loading.Loading
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecastUnit
import cszsm.dolgok.forecast.presentation.components.DailyForecastList
import cszsm.dolgok.forecast.presentation.components.HourlyForecastList
import cszsm.dolgok.forecast.presentation.components.TimeResolutionButtonGroup
import cszsm.dolgok.forecast.presentation.components.WeatherVariableButtonGroup
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
            TimeResolutionButtonGroup(
                selectedResolution = selectedTimeResolution,
                onSelect = onTimeResolutionSelect
            )

            when (selectedTimeResolution) {
                TimeResolution.HOURLY ->
                    if (hourlyForecast == null) {
                        Loading()
                    } else {
                        WeatherVariableButtonGroup(
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
                        WeatherVariableButtonGroup(
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

private fun getPreviewTime(dayOfMonth: Int, hour: Int) =
    LocalDateTime(year = 2025, monthNumber = 3, dayOfMonth = dayOfMonth, hour = hour, minute = 0)

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
