package cszsm.dolgok.forecast.presentation

import cszsm.dolgok.core.domain.models.FetchedData
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import cszsm.dolgok.forecast.domain.usecases.FetchDailyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.FetchFirstDayHourlyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.FetchMoreHourlyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.IsMoreHourlyForecastAllowedUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// TODO: remove the any()s once the coordinates are no longer hardcoded in the viewmodel
@OptIn(ExperimentalCoroutinesApi::class)
internal class ForecastViewModelTest {

    private val mockForecastRepository: ForecastRepository = mockk(relaxed = true)
    private val mockFetchFirstDayHourlyForecastUseCase: FetchFirstDayHourlyForecastUseCase =
        mockk(relaxed = true)
    private val mockFetchMoreHourlyForecastUseCase: FetchMoreHourlyForecastUseCase =
        mockk(relaxed = true)
    private val mockFetchDailyForecastUseCase: FetchDailyForecastUseCase = mockk(relaxed = true)
    private val mockIsMoreHourlyForecastAllowedUseCase: IsMoreHourlyForecastAllowedUseCase = mockk()

    private lateinit var forecastViewModel: ForecastViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher(TestCoroutineScheduler()))
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `event onTimeResolutionChange with HOURLY should change the state and if the data is not fetched yet, should fetch it`() =
        runTest {
            // Given
            initForecastViewModel(hourlyForecast = FetchedData(), dailyForecast = FetchedData())

            // When
            forecastViewModel.onEvent(ForecastScreenEvent.TimeResolutionChange(TimeResolution.HOURLY))
            advanceUntilIdle()

            // Then
            assertEquals(
                forecastViewModel.state.value.selectedTimeResolution,
                TimeResolution.HOURLY,
            )
            coVerify {
                mockFetchFirstDayHourlyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                )
            }
        }

    @Test
    fun `event onTimeResolutionChange with HOURLY should change the state and if the data is fetched already, should not fetch it`() =
        runTest {
            // Given
            initForecastViewModel(
                hourlyForecast = FetchedData(HOURLY_FORECAST),
                dailyForecast = FetchedData()
            )

            // When
            forecastViewModel.onEvent(ForecastScreenEvent.TimeResolutionChange(TimeResolution.HOURLY))
            advanceUntilIdle()

            // Then
            assertEquals(
                forecastViewModel.state.value.selectedTimeResolution,
                TimeResolution.HOURLY,
            )
            coVerify(exactly = 0) {
                mockFetchFirstDayHourlyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                )
            }
        }

    @Test
    fun `event onTimeResolutionChange with DAILY should change the state and if the data is not fetched yet, should fetch it`() =
        runTest {
            // Given
            initForecastViewModel(hourlyForecast = FetchedData(), dailyForecast = FetchedData())

            // When
            forecastViewModel.onEvent(ForecastScreenEvent.TimeResolutionChange(TimeResolution.DAILY))
            advanceUntilIdle()

            // Then
            assertEquals(
                forecastViewModel.state.value.selectedTimeResolution,
                TimeResolution.DAILY,
            )
            coVerify {
                mockFetchDailyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                )
            }
        }

    @Test
    fun `event onTimeResolutionChange with DAILY should change the state and if the data is fetched already, should not fetch it`() =
        runTest {
            // Given
            initForecastViewModel(
                hourlyForecast = FetchedData(), dailyForecast = FetchedData(
                    DAILY_FORECAST
                )
            )

            // When
            forecastViewModel.onEvent(ForecastScreenEvent.TimeResolutionChange(TimeResolution.DAILY))
            advanceUntilIdle()

            // Then
            assertEquals(
                forecastViewModel.state.value.selectedTimeResolution,
                TimeResolution.DAILY,
            )
            coVerify(exactly = 0) {
                mockFetchDailyForecastUseCase(
                    latitude = any(),
                    longitude = any(),
                )
            }
        }

    @Test
    fun `event onWeatherVariableChange should change the state`() =
        runTest {
            // Given
            initForecastViewModel(
                hourlyForecast = FetchedData(HOURLY_FORECAST),
                dailyForecast = FetchedData()
            )
            forecastViewModel.onEvent(ForecastScreenEvent.TimeResolutionChange(timeResolution = TimeResolution.HOURLY))

            // When
            forecastViewModel.onEvent(ForecastScreenEvent.WeatherVariableChange(weatherVariable = WeatherVariable.RAIN))

            // Then
            assertEquals(
                forecastViewModel.state.value.selectedWeatherVariable,
                WeatherVariable.RAIN,
            )
        }

    @Test
    fun `event HourlyForecastEndReach should load more forecast if allowed`() = runTest {
        // Given
        initForecastViewModel(
            hourlyForecast = FetchedData(HOURLY_FORECAST),
            dailyForecast = FetchedData(),
        )

        // When
        forecastViewModel.onEvent(ForecastScreenEvent.HourlyForecastEndReach)
        advanceUntilIdle()

        // Then
        coVerify {
            mockFetchMoreHourlyForecastUseCase(
                latitude = any(),
                longitude = any(),
                lastLoadedDateTime = LOCAL_DATE_TIME,
            )
        }
    }

    @Test
    fun `event HourlyForecastEndReach should not load more forecast if not allowed`() = runTest {
        // Given
        initForecastViewModel(
            hourlyForecast = FetchedData(HOURLY_FORECAST),
            dailyForecast = FetchedData(),
            moreHourlyForecastAllowed = false,
        )

        // When
        forecastViewModel.onEvent(ForecastScreenEvent.HourlyForecastEndReach)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 0) {
            mockFetchMoreHourlyForecastUseCase(
                latitude = any(),
                longitude = any(),
                lastLoadedDateTime = LOCAL_DATE_TIME,
            )
        }
    }

    // TODO: check whether testing streams could be improved
    private fun TestScope.initForecastViewModel(
        hourlyForecast: FetchedData<HourlyForecast>,
        dailyForecast: FetchedData<DailyForecast>,
        moreHourlyForecastAllowed: Boolean = true,
    ) {
        every {
            mockForecastRepository.hourlyForecastStream
        } returns MutableStateFlow(hourlyForecast)
        every {
            mockForecastRepository.dailyForecastStream
        } returns MutableStateFlow(dailyForecast)
        every {
            mockIsMoreHourlyForecastAllowedUseCase(lastLoadedForecastDateTime = LOCAL_DATE_TIME)
        } returns moreHourlyForecastAllowed

        forecastViewModel = ForecastViewModel(
            forecastRepository = mockForecastRepository,
            fetchFirstDayHourlyForecastUseCase = mockFetchFirstDayHourlyForecastUseCase,
            fetchMoreHourlyForecastUseCase = mockFetchMoreHourlyForecastUseCase,
            fetchDailyForecastUseCase = mockFetchDailyForecastUseCase,
            isMoreHourlyForecastAllowedUseCase = mockIsMoreHourlyForecastAllowedUseCase,
        )
        advanceUntilIdle()
    }

    private companion object {
        val LOCAL_DATE = LocalDate(year = 2025, monthNumber = 10, dayOfMonth = 20)
        val LOCAL_DATE_TIME = LocalDateTime(
            date = LOCAL_DATE,
            time = LocalTime(hour = 16, minute = 0, second = 0, nanosecond = 0),
        )
        val HOURLY_FORECAST = HourlyForecast(
            hours = mapOf(
                LOCAL_DATE_TIME to HourlyForecast.Variables(
                    temperature = 13.8f,
                    rain = 0f,
                    pressure = 1001.4f,
                )
            )
        )
        val DAILY_FORECAST = DailyForecast(
            days = mapOf(
                LOCAL_DATE to DailyForecast.Variables(
                    temperatureMax = 13.8f,
                    temperatureMin = 4.8f,
                    rainSum = 0f,
                )
            )
        )
    }
}