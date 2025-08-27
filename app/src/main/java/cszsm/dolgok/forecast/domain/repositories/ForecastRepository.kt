package cszsm.dolgok.forecast.domain.repositories

import cszsm.dolgok.core.domain.model.DateTimeInterval
import cszsm.dolgok.core.domain.model.LoadedData
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import kotlinx.coroutines.flow.StateFlow

interface ForecastRepository {

    val hourlyForecastStream: StateFlow<LoadedData<HourlyForecast>?>
    val dailyForecastStream: StateFlow<LoadedData<DailyForecast>?>

    suspend fun fetchHourlyForecast(
        latitude: Float,
        longitude: Float,
        timeInterval: DateTimeInterval,
    )

    suspend fun fetchDailyForecast(
        latitude: Float,
        longitude: Float,
    )
}