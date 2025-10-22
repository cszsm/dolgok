package cszsm.dolgok.forecast.data.datasources

import cszsm.dolgok.forecast.data.models.DailyForecastApiModel
import cszsm.dolgok.forecast.data.models.HourlyForecastApiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

internal interface WeatherDataSource {

    // https://api.open-meteo.com/v1//forecast?latitude=47.5&longitude=19.04&timezone=auto&hourly=temperature_2m%2Crain%2Csurface_pressure&start_hour=2025-06-27T13%3A00&end_hour=2025-06-28T12%3A00
    suspend fun getHourlyForecast(
        latitude: Float,
        longitude: Float,
        startHour: LocalDateTime,
        endHour: LocalDateTime,
    ): HourlyForecastApiModel

    // https://api.open-meteo.com/v1//forecast?latitude=47.5&longitude=19.04&timezone=auto&daily=temperature_2m_max%2Ctemperature_2m_min%2Crain_sum&start_date=2025-10-22&end_date=2025-10-31
    suspend fun getDailyForecast(
        latitude: Float,
        longitude: Float,
        startDate: LocalDate,
        endDate: LocalDate,
    ): DailyForecastApiModel
}