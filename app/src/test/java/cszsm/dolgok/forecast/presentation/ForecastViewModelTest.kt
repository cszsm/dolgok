package cszsm.dolgok.forecast.presentation

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.ForecastDay
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.usecases.GetDailyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.GetHourlyForecastUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// TODO: remove the any()s once the coordinates are no longer hardcoded in the viewmodel
@OptIn(ExperimentalCoroutinesApi::class)
class ForecastViewModelTest {

    private val mockGetHourlyForecastUseCase: GetHourlyForecastUseCase = mockk()
    private val mockGetDailyForecastUseCase: GetDailyForecastUseCase = mockk()

    private lateinit var forecastViewModel: ForecastViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher(TestCoroutineScheduler()))

        forecastViewModel = ForecastViewModel(
            getHourlyForecastUseCase = mockGetHourlyForecastUseCase,
            getDailyForecastUseCase = mockGetDailyForecastUseCase,
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onTimeResolutionChange should change the state and if the data is not fetched yet, should fetch it`() =
        runTest {
            // Given
            coEvery {
                mockGetHourlyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                    ForecastDay.TODAY,
                )
            } returns HOURLY_RESULT_FOR_TODAY
            coEvery {
                mockGetDailyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                )
            } returns DAILY_RESULT

            forecastViewModel.onTimeResolutionChange(selectedTimeResolution = TimeResolution.HOURLY)
            advanceUntilIdle()

            // When
            forecastViewModel.onTimeResolutionChange(selectedTimeResolution = TimeResolution.DAILY)
            advanceUntilIdle()
            forecastViewModel.onTimeResolutionChange(selectedTimeResolution = TimeResolution.HOURLY)
            advanceUntilIdle()
            forecastViewModel.onTimeResolutionChange(selectedTimeResolution = TimeResolution.DAILY)
            advanceUntilIdle()
            forecastViewModel.onTimeResolutionChange(selectedTimeResolution = TimeResolution.HOURLY)
            advanceUntilIdle()
            forecastViewModel.onTimeResolutionChange(selectedTimeResolution = TimeResolution.DAILY)
            advanceUntilIdle()

            // Then
            coVerify(exactly = 1) {
                mockGetHourlyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                    forecastDay = ForecastDay.TODAY,
                )
            }
            coVerify(exactly = 1) {
                mockGetDailyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                )
            }
            assertEquals(
                forecastViewModel.state.value.selectedTimeResolution,
                TimeResolution.DAILY,
            )
            assertEquals(
                forecastViewModel.state.value.dailyForecast,
                DAILY_RESULT,
            )
        }

    @Test
    fun `onWeatherVariableChange should change the state`() =
        runTest {
            // Given
            coEvery {
                mockGetHourlyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                    ForecastDay.TODAY,
                )
            } returns HOURLY_RESULT_FOR_TODAY

            forecastViewModel.onTimeResolutionChange(selectedTimeResolution = TimeResolution.HOURLY)
            advanceUntilIdle()

            // When
            forecastViewModel.onWeatherVariableChange(
                selectedWeatherVariable = WeatherVariable.RAIN,
            )

            // Then
            assertEquals(
                forecastViewModel.state.value.selectedWeatherVariable,
                WeatherVariable.RAIN,
            )
        }

    @Test
    fun `loadHourlyForecastForTheNextDay should load the forecast for the following day, if the last loaded day was loaded successfully`() =
        runTest {
            // Given
            coEvery {
                mockGetHourlyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                    ForecastDay.TODAY,
                )
            } returns HOURLY_RESULT_FOR_TODAY
            coEvery {
                mockGetHourlyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                    ForecastDay.TOMORROW,
                )
            } returns HOURLY_RESULT_FOR_TOMORROW

            forecastViewModel.onTimeResolutionChange(selectedTimeResolution = TimeResolution.HOURLY)
            advanceUntilIdle()

            // When
            forecastViewModel.loadHourlyForecastForTheNextDay()
            advanceUntilIdle()

            // Then
            val actual = forecastViewModel.state.value.hourlyForecastByDay
            val expected = mapOf(
                ForecastDay.TODAY to HOURLY_RESULT_FOR_TODAY,
                ForecastDay.TOMORROW to HOURLY_RESULT_FOR_TOMORROW,
            )
            assertEquals(expected, actual)
        }

    @Test
    fun `loadHourlyForecastForTheNextDay should not load the forecast for the following day, if the last loaded day was failed to load`() =
        runTest {
            // Given
            coEvery {
                mockGetHourlyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                    ForecastDay.TODAY,
                )
            } returns HOURLY_RESULT_ERROR

            forecastViewModel.onTimeResolutionChange(selectedTimeResolution = TimeResolution.HOURLY)
            advanceUntilIdle()

            // When
            forecastViewModel.loadHourlyForecastForTheNextDay()
            advanceUntilIdle()

            // Then
            coVerify(exactly = 0) {
                mockGetHourlyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                    ForecastDay.TOMORROW,
                )
            }
        }

    private companion object {
        val HOURLY_RESULT_FOR_TODAY = Result.Success<HourlyForecast, DataError>(mockk())
        val HOURLY_RESULT_FOR_TOMORROW = Result.Success<HourlyForecast, DataError>(mockk())
        val HOURLY_RESULT_ERROR = Result.Failure<HourlyForecast, DataError>(mockk())
        val DAILY_RESULT = Result.Success<DailyForecast, DataError>(mockk())
    }
}