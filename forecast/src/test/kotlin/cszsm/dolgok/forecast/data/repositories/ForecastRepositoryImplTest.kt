package cszsm.dolgok.forecast.data.repositories

import android.util.Log
import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.models.DateInterval
import cszsm.dolgok.core.domain.models.DateTimeInterval
import cszsm.dolgok.core.domain.models.FetchedData
import cszsm.dolgok.forecast.data.datasources.WeatherDataSource
import cszsm.dolgok.forecast.data.mappers.DailyForecastMapper
import cszsm.dolgok.forecast.data.mappers.HourlyForecastMapper
import cszsm.dolgok.forecast.data.models.DailyForecastApiModel
import cszsm.dolgok.forecast.data.models.HourlyForecastApiModel
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.io.IOException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ForecastRepositoryImplTest {

    private val mockWeatherDataSource: WeatherDataSource = mockk()
    private val mockHourlyForecastMapper: HourlyForecastMapper = mockk()
    private val mockDailyForecastMapper: DailyForecastMapper = mockk()

    private lateinit var forecastRepositoryImpl: ForecastRepositoryImpl

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher(TestCoroutineScheduler()))

        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0

        forecastRepositoryImpl = ForecastRepositoryImpl(
            weatherDataSource = mockWeatherDataSource,
            hourlyForecastMapper = mockHourlyForecastMapper,
            dailyForecastMapper = mockDailyForecastMapper,
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchHourlyForecast should update the state with the returned data`() =
        runTest {
            // Given
            coEvery {
                mockWeatherDataSource.getHourlyForecast(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                    startHour = START_HOUR,
                    endHour = END_HOUR,
                )
            } returns HOURLY_FORECAST_API_MODEL
            every {
                mockHourlyForecastMapper.map(input = HOURLY_FORECAST_API_MODEL)
            } returns HOURLY_FORECAST

            // When
            forecastRepositoryImpl.fetchHourlyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                timeInterval = DateTimeInterval(START_HOUR, END_HOUR),
            )
            advanceUntilIdle()

            // Then
            val actual = forecastRepositoryImpl.hourlyForecastStream.value
            val expected = FetchedData(data = HOURLY_FORECAST)
            assertEquals(expected, actual)
        }

    @Test
    fun `fetchHourlyForecast should cause a NETWORK error if an IOException is caught`() =
        runTest {
            // Given
            coEvery {
                mockWeatherDataSource.getHourlyForecast(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                    startHour = START_HOUR,
                    endHour = END_HOUR,
                )
            } throws IOException()

            // When
            forecastRepositoryImpl.fetchHourlyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                timeInterval = DateTimeInterval(START_HOUR, END_HOUR),
            )
            advanceUntilIdle()

            // Then
            val actual = forecastRepositoryImpl.hourlyForecastStream.value
            val expected = FetchedData<HourlyForecast>(error = DataError.NETWORK)
            assertEquals(expected, actual)
        }

    @Test
    fun `fetchHourlyForecast should cause an UNKNOWN error if an Exception is caught`() =
        runTest {
            // Given
            coEvery {
                mockWeatherDataSource.getHourlyForecast(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                    startHour = START_HOUR,
                    endHour = END_HOUR,
                )
            } returns HOURLY_FORECAST_API_MODEL
            every {
                mockHourlyForecastMapper.map(input = HOURLY_FORECAST_API_MODEL)
            } throws Exception()

            // When
            forecastRepositoryImpl.fetchHourlyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                timeInterval = DateTimeInterval(START_HOUR, END_HOUR),
            )
            advanceUntilIdle()

            // Then
            val actual = forecastRepositoryImpl.hourlyForecastStream.value
            val expected = FetchedData<HourlyForecast>(error = DataError.UNKNOWN)
            assertEquals(expected, actual)
        }

    @Test
    fun `fetchDailyForecast should update the state with the returned data`() =
        runTest {
            // Given
            coEvery {
                mockWeatherDataSource.getDailyForecast(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                    startDate = START_DATE,
                    endDate = END_DATE,
                )
            } returns DAILY_FORECAST_API_MODEL
            every {
                mockDailyForecastMapper.map(input = DAILY_FORECAST_API_MODEL)
            } returns DAILY_FORECAST

            // When
            forecastRepositoryImpl.fetchDailyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                dateInterval = DateInterval(START_DATE, END_DATE),
            )
            advanceUntilIdle()

            // Then
            val actual = forecastRepositoryImpl.dailyForecastStream.value
            val expected = FetchedData(data = DAILY_FORECAST)
            assertEquals(expected, actual)
        }

    @Test
    fun `fetchDailyForecast should cause an NETWORK error if an IOException is caught`() =
        runTest {
            // Given
            coEvery {
                mockWeatherDataSource.getDailyForecast(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                    startDate = START_DATE,
                    endDate = END_DATE,
                )
            } throws IOException()

            // When
            forecastRepositoryImpl.fetchDailyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                dateInterval = DateInterval(START_DATE, END_DATE),
            )
            advanceUntilIdle()

            // Then
            val actual = forecastRepositoryImpl.dailyForecastStream.value
            val expected = FetchedData<DailyForecast>(error = DataError.NETWORK)
            assertEquals(expected, actual)
        }

    @Test
    fun `fetchDailyForecast should cause an UNKNOWN error if an Exception is caught`() =
        runTest {
            // Given
            coEvery {
                mockWeatherDataSource.getDailyForecast(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                    startDate = START_DATE,
                    endDate = END_DATE,
                )
            } returns DAILY_FORECAST_API_MODEL
            every {
                mockDailyForecastMapper.map(input = DAILY_FORECAST_API_MODEL)
            } throws Exception()

            // When
            forecastRepositoryImpl.fetchDailyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                dateInterval = DateInterval(START_DATE, END_DATE),
            )
            advanceUntilIdle()

            // Then
            val actual = forecastRepositoryImpl.dailyForecastStream.value
            val expected = FetchedData<DailyForecast>(error = DataError.UNKNOWN)
            assertEquals(expected, actual)
        }

    private companion object {
        const val LATITUDE = 47.5f
        const val LONGITUDE = 19.04f
        val START_HOUR = LocalDateTime.parse("2025-06-27T13:00")
        val END_HOUR = LocalDateTime.parse("2025-06-28T12:00")
        val START_DATE = LocalDate.parse("2025-10-22")
        val END_DATE = LocalDate.parse("2025-10-27")

        val HOURLY_FORECAST_API_MODEL: HourlyForecastApiModel = mockk()
        val DAILY_FORECAST_API_MODEL: DailyForecastApiModel = mockk()
        val HOURLY_FORECAST: HourlyForecast = mockk()
        val DAILY_FORECAST: DailyForecast = mockk()
    }
}