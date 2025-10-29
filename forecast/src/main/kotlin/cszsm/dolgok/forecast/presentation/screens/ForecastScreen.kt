@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package cszsm.dolgok.forecast.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cszsm.dolgok.core.domain.models.FetchedData
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.presentation.ForecastScreenEvent
import cszsm.dolgok.forecast.presentation.ForecastScreenState
import cszsm.dolgok.forecast.presentation.ForecastViewModel
import cszsm.dolgok.forecast.presentation.TimeResolution
import cszsm.dolgok.forecast.presentation.WeatherVariable
import cszsm.dolgok.forecast.presentation.components.DailyForecastSection
import cszsm.dolgok.forecast.presentation.components.HourlyForecastSection
import cszsm.dolgok.forecast.presentation.components.TimeResolutionTabRow
import cszsm.dolgok.forecast.presentation.components.WeatherVariableButtonGroup
import cszsm.dolgok.localization.R
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ForecastScreen(
    viewModel: ForecastViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    ForecastContent(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForecastContent(
    state: ForecastScreenState,
    onEvent: (ForecastScreenEvent) -> Unit,
) {
    val timeResolutions = TimeResolution.entries

    val pagerState = rememberPagerState { timeResolutions.size }

    LaunchedEffect(state.selectedTimeResolution) {
        pagerState.animateScrollToPage(state.selectedTimeResolution.ordinal)
    }
    LaunchedEffect(pagerState.currentPage) {
        onEvent(ForecastScreenEvent.TimeResolutionChange(timeResolution = TimeResolution.entries[pagerState.currentPage]))
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.forecast_title))
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                WeatherVariableButtonGroup(
                    weatherVariables = state.selectedTimeResolution.weatherVariables,
                    selectedWeatherVariable = state.selectedWeatherVariable,
                    onSelect = { onEvent(ForecastScreenEvent.WeatherVariableChange(weatherVariable = it)) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {

            TimeResolutionTabRow(
                timeResolutions = timeResolutions,
                selectedTimeResolution = state.selectedTimeResolution,
                onSelect = { onEvent(ForecastScreenEvent.TimeResolutionChange(timeResolution = it)) }
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
                    hourlyForecastSectionState = state.hourlyForecastSectionState,
                    dailyForecastSectionState = state.dailyForecastSectionState,
                    selectedWeatherVariable = state.selectedWeatherVariable,
                    onHourlyForecastEndReach = { onEvent(ForecastScreenEvent.HourlyForecastEndReach) },
                )
            }
        }
    }
}

@Composable
private fun ForecastPage(
    selectedTimeResolution: TimeResolution,
    hourlyForecastSectionState: ForecastScreenState.HourlyForecastSectionState,
    dailyForecastSectionState: ForecastScreenState.DailyForecastSectionState,
    selectedWeatherVariable: WeatherVariable,
    onHourlyForecastEndReach: () -> Unit,
) {
    when (selectedTimeResolution) {
        TimeResolution.HOURLY -> HourlyForecastSection(
            state = hourlyForecastSectionState,
            selectedWeatherVariable = selectedWeatherVariable,
            onEndReach = onHourlyForecastEndReach,
        )

        TimeResolution.DAILY -> DailyForecastSection(
            state = dailyForecastSectionState,
            selectedWeatherVariable = selectedWeatherVariable,
        )
    }
}

private fun getPreviewTime(day: Int, hour: Int) =
    LocalDateTime(year = 2025, month = 3, day = day, hour = hour, minute = 0)

@Preview
@Composable
private fun ForecastContent_Preview() {
    val hourlyForecast = HourlyForecast(
        hours = mapOf(
            getPreviewTime(day = 22, hour = 22) to
                    HourlyForecast.Variables(
                        temperature = 11.4f,
                        rain = 0f,
                        pressure = 999.7f,
                    ),
            getPreviewTime(day = 22, hour = 23) to
                    HourlyForecast.Variables(
                        temperature = 11.7f,
                        rain = 0f,
                        pressure = 998.6f,
                    ),
            getPreviewTime(day = 23, hour = 0) to
                    HourlyForecast.Variables(
                        temperature = 10.2f,
                        rain = 0.3f,
                        pressure = 997f,
                    ),
            getPreviewTime(day = 23, hour = 1) to
                    HourlyForecast.Variables(
                        temperature = 9.9f,
                        rain = 0.1f,
                        pressure = 996.4f,
                    ),
        ),
        units = HourlyForecast.Units(
            temperature = "${Typography.degree}C",
            rain = "mm",
            pressure = "hPa",
        )
    )
    ForecastContent(
        state = ForecastScreenState(
            hourlyForecastSectionState = ForecastScreenState.HourlyForecastSectionState(
                forecast = FetchedData(data = hourlyForecast)
            ),
        ),
        onEvent = {},
    )
}
