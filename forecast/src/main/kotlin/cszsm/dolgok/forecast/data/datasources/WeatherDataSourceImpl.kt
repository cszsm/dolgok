package cszsm.dolgok.forecast.data.datasources

import cszsm.dolgok.forecast.data.models.DailyForecastApiModel
import cszsm.dolgok.forecast.data.models.ForecastRequest
import cszsm.dolgok.forecast.data.models.HourlyForecastApiModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

internal class WeatherDataSourceImpl(
    private val client: HttpClient,
) : WeatherDataSource {

    override suspend fun getHourlyForecast(
        latitude: Float,
        longitude: Float,
        startHour: LocalDateTime,
        endHour: LocalDateTime,
    ): HourlyForecastApiModel =
        client.get(
            ForecastRequest(
                latitude = latitude,
                longitude = longitude,
                start_hour = startHour,
                end_hour = endHour,
                hourly = "temperature_2m,rain,surface_pressure",
            )
        ).body()

    override suspend fun getDailyForecast(
        latitude: Float,
        longitude: Float,
        startDate: LocalDate,
        endDate: LocalDate,
    ): DailyForecastApiModel =
        client.get(
            ForecastRequest(
                latitude = latitude,
                longitude = longitude,
                start_date = startDate,
                end_date = endDate,
                daily = "temperature_2m_max,temperature_2m_min,rain_sum",
            )
        ).body()
}