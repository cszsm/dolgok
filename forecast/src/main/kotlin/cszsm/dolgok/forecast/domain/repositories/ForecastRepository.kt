package cszsm.dolgok.forecast.domain.repositories

import cszsm.dolgok.core.domain.models.DateInterval
import cszsm.dolgok.core.domain.models.DateTimeInterval
import cszsm.dolgok.core.domain.models.FetchedData
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import kotlinx.coroutines.flow.StateFlow

internal interface ForecastRepository {

    val hourlyForecastStream: StateFlow<FetchedData<HourlyForecast>>
    val dailyForecastStream: StateFlow<FetchedData<DailyForecast>>

    suspend fun fetchHourlyForecast(
        latitude: Float,
        longitude: Float,
        timeInterval: DateTimeInterval,
    )

    suspend fun fetchDailyForecast(
        latitude: Float,
        longitude: Float,
        dateInterval: DateInterval,
    )
}