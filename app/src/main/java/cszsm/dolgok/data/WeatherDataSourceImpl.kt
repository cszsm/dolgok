package cszsm.dolgok.data

import android.util.Log
import cszsm.dolgok.data.dto.ForecastApiModel
import cszsm.dolgok.data.requests.ForecastRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.resources.get
import kotlinx.datetime.LocalDateTime

class WeatherDataSourceImpl(
    private val client: HttpClient,
) : WeatherDataSource {

    override suspend fun getForecast(
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