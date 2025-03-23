package cszsm.dolgok.data

import cszsm.dolgok.data.dto.ForecastResponse
import kotlinx.datetime.LocalDateTime

interface WeatherService {

    suspend fun getForecast(
        latitude: Float,
        longitude: Float,
        startHour: LocalDateTime,
        endHour: LocalDateTime,
    ): ForecastResponse?
}