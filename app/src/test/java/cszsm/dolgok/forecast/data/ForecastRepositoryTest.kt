package cszsm.dolgok.forecast.data

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.data.models.ForecastApiModel
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ForecastRepositoryTest {

    private val mockWeatherDataSource: WeatherDataSource = mockk()
    private val mockForecastTransformer: ForecastTransformer = mockk()

    private lateinit var forecastRepository: ForecastRepository

    @BeforeEach
    fun setUp() {
        forecastRepository = ForecastRepository(
            weatherDataSource = mockWeatherDataSource,
            forecastTransformer = mockForecastTransformer,
        )
    }

    @Test
    fun `fetchHourlyForecast should return success result if the data source returns proper data`() =
        runTest {
            // Given
            coEvery {
                mockWeatherDataSource.getHourlyForecast(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                    startHour = START_HOUR,
                    endHour = END_HOUR,
                )
            } returns FORECAST_API_MODEL
            every {
                mockForecastTransformer.transformHourly(response = FORECAST_API_MODEL)
            } returns Result.Success(data = HOURLY_FORECAST)

            // When
            val actual = forecastRepository.fetchHourlyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                startHour = START_HOUR,
                endHour = END_HOUR,
            )

            // Then
            val expected = Result.Success<HourlyForecast, DataError>(data = HOURLY_FORECAST)
            assertEquals(expected, actual)
        }

    @Test
    fun `fetchHourlyForecast should return error result if the network call fails`() =
        runTest {
            // Given
            coEvery {
                mockWeatherDataSource.getHourlyForecast(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                    startHour = START_HOUR,
                    endHour = END_HOUR,
                )
            } throws Exception()

            // When
            val actual = forecastRepository.fetchHourlyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                startHour = START_HOUR,
                endHour = END_HOUR,
            )

            // Then
            val expected = Result.Failure<HourlyForecast, DataError>(error = DataError.NETWORK)
            assertEquals(expected, actual)
        }

    @Test
    fun `fetchHourlyForecast should return error result if the data source returns improper data`() =
        runTest {
            // Given
            coEvery {
                mockWeatherDataSource.getHourlyForecast(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                    startHour = START_HOUR,
                    endHour = END_HOUR,
                )
            } returns null
            every {
                mockForecastTransformer.transformHourly(response = null)
            } returns Result.Failure(error = DataError.INCOMPLETE_DATA)

            // When
            val actual = forecastRepository.fetchHourlyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                startHour = START_HOUR,
                endHour = END_HOUR,
            )

            // Then
            val expected =
                Result.Failure<HourlyForecast, DataError>(error = DataError.INCOMPLETE_DATA)
            assertEquals(expected, actual)
        }

    @Test
    fun `fetchDailyForecast should return success result if the data source returns proper data`() =
        runTest {
            // Given
            coEvery {
                mockWeatherDataSource.getDailyForecast(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                )
            } returns FORECAST_API_MODEL
            every {
                mockForecastTransformer.transformDaily(response = FORECAST_API_MODEL)
            } returns Result.Success(data = DAILY_FORECAST)

            // When
            val actual = forecastRepository.fetchDailyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
            )

            // Then
            val expected = Result.Success<DailyForecast, DataError>(data = DAILY_FORECAST)
            assertEquals(expected, actual)
        }

    @Test
    fun `fetchDailyForecast should return error result if the data source returns improper data`() =
        runTest {
            // Given
            coEvery {
                mockWeatherDataSource.getDailyForecast(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                )
            } returns null
            every {
                mockForecastTransformer.transformDaily(response = null)
            } returns Result.Failure(error = DataError.INCOMPLETE_DATA)

            // When
            val actual = forecastRepository.fetchDailyForecast(
                latitude = LATITUDE,
                longitude = LONGITUDE,
            )

            // Then
            val expected =
                Result.Failure<DailyForecast, DataError>(error = DataError.INCOMPLETE_DATA)
            assertEquals(expected, actual)
        }

    private companion object {
        const val LATITUDE = 47.5f
        const val LONGITUDE = 19.04f
        val START_HOUR = LocalDateTime.parse("2025-06-27T13:00")
        val END_HOUR = LocalDateTime.parse("2025-06-28T12:00")

        val FORECAST_API_MODEL: ForecastApiModel = mockk()
        val HOURLY_FORECAST: HourlyForecast = mockk()
        val DAILY_FORECAST: DailyForecast = mockk()
    }
}