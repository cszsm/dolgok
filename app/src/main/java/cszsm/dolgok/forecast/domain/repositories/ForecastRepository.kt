package cszsm.dolgok.forecast.domain.repositories

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import kotlinx.datetime.LocalDateTime

interface ForecastRepository {

    suspend fun fetchHourlyForecast(
        latitude: Float,
        longitude: Float,
        startHour: LocalDateTime,
        endHour: LocalDateTime,
    ): Result<HourlyForecast, DataError>

    suspend fun fetchDailyForecast(
        latitude: Float,
        longitude: Float,
    ): Result<DailyForecast, DataError>
}