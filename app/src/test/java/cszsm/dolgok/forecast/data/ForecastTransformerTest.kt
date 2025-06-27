package cszsm.dolgok.forecast.data

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.data.models.ForecastApiModel
import cszsm.dolgok.forecast.data.models.ForecastDataApiModel
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.DailyForecastUnit
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecastUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.FieldSource

class ForecastTransformerTest {

    private lateinit var forecastTransformer: ForecastTransformer

    @BeforeEach
    fun setUp() {
        forecastTransformer = ForecastTransformer()
    }

    @ParameterizedTest
    @FieldSource("transformHourlyTestData")
    fun transformHourly(
        data: Pair<ForecastApiModel, Result<HourlyForecast, DataError>>,
    ) {
        // Given
        val response = data.first

        // When
        val actual = forecastTransformer.transformHourly(response = response)

        // Then
        val expected = data.second
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @FieldSource("transformDailyTestData")
    fun transformDaily(
        data: Pair<ForecastApiModel, Result<DailyForecast, DataError>>,
    ) {
        // Given
        val response = data.first

        // When
        val actual = forecastTransformer.transformDaily(response = response)

        // Then
        val expected = data.second
        assertEquals(expected, actual)
    }

    private companion object {
        const val TIME_1 = "2025-06-27T13:00"
        const val TIME_2 = "2025-06-27T14:00"
        const val DATE_1 = "2025-06-30"
        const val DATE_2 = "2025-07-01"
        const val TEMPERATURE_1 = 27.9f
        const val TEMPERATURE_2 = 29.2f
        const val TEMPERATURE_MIN_1 = 12.3f
        const val TEMPERATURE_MIN_2 = 11.2f
        const val TEMPERATURE_MAX_1 = 28.4f
        const val TEMPERATURE_MAX_2 = 29.4f
        const val RAIN_1 = 0.1f
        const val RAIN_2 = 0.3f
        const val RAIN_SUM_1 = 1.1f
        const val RAIN_SUM_2 = 0.8f
        const val PRESSURE_1 = 1004.4f
        const val PRESSURE_2 = 1004.1f

        private val RESPONSE = object {
            val HOURLY = object {
                val COMPLETE = ForecastApiModel(
                    hourly = ForecastDataApiModel(
                        time = listOf(TIME_1, TIME_2),
                        temperature_2m = listOf(TEMPERATURE_1, TEMPERATURE_2),
                        rain = listOf(RAIN_1, RAIN_2),
                        surface_pressure = listOf(PRESSURE_1, PRESSURE_2),
                    )
                )
                val TIME_WRONG_DATA_FORMAT = ForecastApiModel(
                    hourly = ForecastDataApiModel(
                        time = listOf(TIME_1, "invalid time"),
                        temperature_2m = listOf(TEMPERATURE_1, TEMPERATURE_2),
                        rain = listOf(RAIN_1, RAIN_2),
                        surface_pressure = listOf(PRESSURE_1, PRESSURE_2),
                    )
                )
                val TEMPERATURE_INCOMPLETE = ForecastApiModel(
                    hourly = ForecastDataApiModel(
                        time = listOf(TIME_1, TIME_2),
                        temperature_2m = listOf(TEMPERATURE_1),
                        rain = listOf(RAIN_1, RAIN_2),
                        surface_pressure = listOf(PRESSURE_1, PRESSURE_2),
                    )
                )
            }
            val DAILY = object {
                val COMPLETE = ForecastApiModel(
                    daily = ForecastDataApiModel(
                        time = listOf(DATE_1, DATE_2),
                        temperature_2m_min = listOf(TEMPERATURE_MIN_1, TEMPERATURE_MIN_2),
                        temperature_2m_max = listOf(TEMPERATURE_MAX_1, TEMPERATURE_MAX_2),
                        rain_sum = listOf(RAIN_SUM_1, RAIN_SUM_2)
                    )
                )
                val TIME_WRONG_DATA_FORMAT = ForecastApiModel(
                    daily = ForecastDataApiModel(
                        time = listOf(DATE_1, "invalid date"),
                        temperature_2m_min = listOf(TEMPERATURE_MIN_1, TEMPERATURE_MIN_2),
                        temperature_2m_max = listOf(TEMPERATURE_MAX_1, TEMPERATURE_MAX_2),
                        rain_sum = listOf(RAIN_SUM_1, RAIN_SUM_2)
                    )
                )
                val TEMPERATURE_MIN_INCOMPLETE = ForecastApiModel(
                    daily = ForecastDataApiModel(
                        time = listOf(DATE_1, DATE_2),
                        temperature_2m_min = listOf(TEMPERATURE_MIN_1),
                        temperature_2m_max = listOf(TEMPERATURE_MAX_1, TEMPERATURE_MAX_2),
                        rain_sum = listOf(RAIN_SUM_1, RAIN_SUM_2)
                    )
                )
            }
        }
        private val TRANSFORMED = object {
            val HOURLY = object {
                val COMPLETE = HourlyForecast(
                    hours = listOf(
                        HourlyForecastUnit(
                            time = LocalDateTime.parse(TIME_1),
                            temperature = TEMPERATURE_1,
                            rain = RAIN_1,
                            pressure = PRESSURE_1,
                        ),
                        HourlyForecastUnit(
                            time = LocalDateTime.parse(TIME_2),
                            temperature = TEMPERATURE_2,
                            rain = RAIN_2,
                            pressure = PRESSURE_2,
                        ),
                    )
                )
            }
            val DAILY = object {
                val COMPLETE = DailyForecast(
                    days = listOf(
                        DailyForecastUnit(
                            date = LocalDate.parse(DATE_1),
                            temperatureMin = TEMPERATURE_MIN_1,
                            temperatureMax = TEMPERATURE_MAX_1,
                            rainSum = RAIN_SUM_1,
                        ),
                        DailyForecastUnit(
                            date = LocalDate.parse(DATE_2),
                            temperatureMin = TEMPERATURE_MIN_2,
                            temperatureMax = TEMPERATURE_MAX_2,
                            rainSum = RAIN_SUM_2,
                        ),
                    )
                )
            }
        }

        @Suppress("unused")
        val transformHourlyTestData =
            listOf<Pair<ForecastApiModel, Result<HourlyForecast, DataError>>>(
                Pair(
                    RESPONSE.HOURLY.COMPLETE,
                    Result.Success(TRANSFORMED.HOURLY.COMPLETE)
                ),
                Pair(
                    RESPONSE.HOURLY.TIME_WRONG_DATA_FORMAT,
                    Result.Failure(DataError.WRONG_DATA_FORMAT)
                ),
                Pair(
                    RESPONSE.HOURLY.TEMPERATURE_INCOMPLETE,
                    Result.Failure(DataError.INCOMPLETE_DATA)
                ),
            )

        @Suppress("unused")
        val transformDailyTestData =
            listOf<Pair<ForecastApiModel, Result<DailyForecast, DataError>>>(
                Pair(
                    RESPONSE.DAILY.COMPLETE,
                    Result.Success(TRANSFORMED.DAILY.COMPLETE)
                ),
                Pair(
                    RESPONSE.DAILY.TIME_WRONG_DATA_FORMAT,
                    Result.Failure(DataError.WRONG_DATA_FORMAT)
                ),
                Pair(
                    RESPONSE.DAILY.TEMPERATURE_MIN_INCOMPLETE,
                    Result.Failure(DataError.INCOMPLETE_DATA)
                ),
            )
    }
}