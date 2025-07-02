package cszsm.dolgok.forecast.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.core.presentation.components.error.FullScreenError
import cszsm.dolgok.core.presentation.components.loading.FullScreenLoading
import cszsm.dolgok.core.presentation.error.message
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecastUnit
import cszsm.dolgok.forecast.presentation.components.DailyForecastList
import cszsm.dolgok.forecast.presentation.components.HourlyForecastList
import cszsm.dolgok.forecast.presentation.components.TimeResolutionTabRow
import cszsm.dolgok.forecast.presentation.components.WeatherVariableButtonGroup
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    ForecastContent(
        hourlyForecast = state.hourlyForecast,
        dailyForecast = state.dailyForecast,
        selectedTimeResolution = state.selectedTimeResolution,
        selectedWeatherVariable = state.selectedWeatherVariable,
        onTimeResolutionSelect = viewModel::onTimeResolutionChange,
        onWeatherVariableChange = viewModel::onWeatherVariableChange,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForecastContent(
    hourlyForecast: Result<HourlyForecast, DataError>?,
    dailyForecast: Result<DailyForecast, DataError>?,
    selectedTimeResolution: TimeResolution,
    selectedWeatherVariable: WeatherVariable,
    onTimeResolutionSelect: (TimeResolution) -> Unit,
    onWeatherVariableChange: (WeatherVariable) -> Unit,
) {
    val timeResolutions = TimeResolution.entries

    val pagerState = rememberPagerState { timeResolutions.size }

    LaunchedEffect(selectedTimeResolution) {
        pagerState.animateScrollToPage(selectedTimeResolution.ordinal)
    }
    LaunchedEffect(pagerState.currentPage) {
        onTimeResolutionSelect(TimeResolution.entries[pagerState.currentPage])
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Forecast")
                }
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {

            TimeResolutionTabRow(
                timeResolutions = timeResolutions,
                selectedTimeResolution = selectedTimeResolution,
                onSelect = onTimeResolutionSelect
            )

            HorizontalPager(
                state = pagerState,
                key = { index -> timeResolutions[index] },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                ForecastPage(
                    selectedTimeResolution = timeResolutions[index],
                    hourlyForecast = hourlyForecast,
                    dailyForecast = dailyForecast,
                    selectedWeatherVariable = selectedWeatherVariable,
                    onWeatherVariableChange = onWeatherVariableChange,
                )
            }
        }
    }
}

@Composable
fun ForecastPage(
    selectedTimeResolution: TimeResolution,
    hourlyForecast: Result<HourlyForecast, DataError>?,
    dailyForecast: Result<DailyForecast, DataError>?,
    selectedWeatherVariable: WeatherVariable,
    onWeatherVariableChange: (WeatherVariable) -> Unit,
) {
    when (selectedTimeResolution) {
        TimeResolution.HOURLY -> {
            when (hourlyForecast) {
                null -> FullScreenLoading()

                is Result.Success -> HourlyForecastContent(
                    hourlyForecast = hourlyForecast.data,
                    selectedWeatherVariable = selectedWeatherVariable,
                    onWeatherVariableChange = onWeatherVariableChange,
                )

                is Result.Failure -> FullScreenError(message = hourlyForecast.error.message)
            }
        }

        TimeResolution.DAILY -> {
            when (dailyForecast) {
                null -> FullScreenLoading()

                is Result.Success -> DailyForecastContent(
                    dailyForecast = dailyForecast.data,
                    selectedWeatherVariable = selectedWeatherVariable,
                    onWeatherVariableChange = onWeatherVariableChange,
                )

                is Result.Failure -> FullScreenError(message = dailyForecast.error.message)
            }
        }
    }
}

@Composable
private fun HourlyForecastContent(
    hourlyForecast: HourlyForecast,
    selectedWeatherVariable: WeatherVariable,
    onWeatherVariableChange: (WeatherVariable) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxHeight()
    ) {
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
}

@Composable
private fun DailyForecastContent(
    dailyForecast: DailyForecast,
    selectedWeatherVariable: WeatherVariable,
    onWeatherVariableChange: (WeatherVariable) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxHeight()
    ) {
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
        hourlyForecast = Result.Success(data = hourlyForecast),
        dailyForecast = null,
        selectedTimeResolution = TimeResolution.HOURLY,
        selectedWeatherVariable = WeatherVariable.TEMPERATURE,
        onTimeResolutionSelect = {},
        onWeatherVariableChange = {},
    )
}
