package cszsm.dolgok.forecast.data.datasources.mock

import cszsm.dolgok.core.util.plus
import cszsm.dolgok.forecast.data.datasources.WeatherDataSource
import cszsm.dolgok.forecast.data.models.DailyForecastApiModel
import cszsm.dolgok.forecast.data.models.HourlyForecastApiModel
import kotlinx.coroutines.delay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus
import kotlin.random.Random

internal class MockWeatherDataSource : WeatherDataSource {
    override suspend fun getHourlyForecast(
        latitude: Float,
        longitude: Float,
        startHour: LocalDateTime,
        endHour: LocalDateTime,
    ): HourlyForecastApiModel {
        delay(2000)

        val time = createHourlyTimeList(startHour = startHour, endHour = endHour)

        return HourlyForecastApiModel(
            hourly = HourlyForecastApiModel.Variables(
                time = time,
                temperature_2m = createTemperatureList(count = time.size),
                rain = createRainList(count = time.size),
                surface_pressure = createPressureList(count = time.size),
            ),
            hourly_units = HourlyForecastApiModel.Units(
                temperature_2m = "${Typography.degree}C",
                rain = "mm",
                surface_pressure = "hPa",
            ),
        )
    }

    override suspend fun getDailyForecast(
        latitude: Float,
        longitude: Float,
        startDate: LocalDate,
        endDate: LocalDate
    ): DailyForecastApiModel {
        delay(2000)

        val time = createDailyTimeList(startDate = startDate, endDate = endDate)

        return DailyForecastApiModel(
            daily = DailyForecastApiModel.Variables(
                time = time,
                temperature_2m_max = createTemperatureMaxList(count = time.size),
                temperature_2m_min = createTemperatureMinList(count = time.size),
                rain_sum = createRainList(count = time.size),
            ),
            daily_units = DailyForecastApiModel.Units(
                temperature_2m_max = "${Typography.degree}C",
                temperature_2m_min = "${Typography.degree}C",
                rain_sum = "mm",
            ),
        )
    }

    private fun createHourlyTimeList(
        startHour: LocalDateTime,
        endHour: LocalDateTime,
    ) = buildList {
        var hour = startHour

        while (hour <= endHour) {
            add(hour.toString())
            hour = hour.plus(hours = 1)
        }
    }

    private fun createTemperatureList(count: Int) =
        (1..count).map { Random.nextDouble(10.0, 25.0).toFloat() }

    private fun createRainList(count: Int) =
        (1..count).map { Random.nextFloat() }

    private fun createPressureList(count: Int) =
        (1..count).map { Random.nextDouble(990.0, 1010.0).toFloat() }

    private fun createDailyTimeList(
        startDate: LocalDate,
        endDate: LocalDate,
    ) = buildList {
        var date = startDate

        while (date <= endDate) {
            add(date.toString())
            date = date.plus(value = 1, unit = DateTimeUnit.DAY)
        }
    }

    private fun createTemperatureMinList(count: Int) =
        (1..count).map { Random.nextDouble(10.0, 15.0).toFloat() }

    private fun createTemperatureMaxList(count: Int) =
        (1..count).map { Random.nextDouble(20.0, 25.0).toFloat() }
}