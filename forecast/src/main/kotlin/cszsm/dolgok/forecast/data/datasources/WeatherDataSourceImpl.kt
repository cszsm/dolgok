package cszsm.dolgok.forecast.data.datasources

import android.util.Log
import cszsm.dolgok.forecast.data.models.ForecastRequest
import cszsm.dolgok.forecast.data.models.ForecastApiModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.resources.get
import kotlinx.datetime.LocalDateTime

internal class WeatherDataSourceImpl(
    private val client: HttpClient,
) : WeatherDataSource {

    override suspend fun getHourlyForecast(
        latitude: Float,
        longitude: Float,
        startHour: LocalDateTime,
        endHour: LocalDateTime,
    ): ForecastApiModel? {
        return try {
            client.get(
                ForecastRequest(
                    latitude = latitude,
                    longitude = longitude,
                    start_hour = startHour,
                    end_hour = endHour,
                    hourly = "temperature_2m,rain,surface_pressure",
                )
            ).body()
        } catch (e: ResponseException) {
            Log.e(TAG, e.response.status.description)
            null
        }
    }

    override suspend fun getDailyForecast(
        latitude: Float,
        longitude: Float,
    ): ForecastApiModel? {
        return try {
            client.get(
                ForecastRequest(
                    latitude = latitude,
                    longitude = longitude,
                    daily = "temperature_2m_max,temperature_2m_min,rain_sum",
                )
            ).body()
        } catch (e: ResponseException) {
            Log.e(TAG, e.response.status.description)
            null
        }
    }

    private companion object {
        val TAG = WeatherDataSourceImpl::class.simpleName
    }
}