package cszsm.dolgok.forecast.data.repositories

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.model.DateTimeInterval
import cszsm.dolgok.core.domain.model.LoadedData
import cszsm.dolgok.forecast.data.datasources.WeatherDataSource
import cszsm.dolgok.forecast.data.mappers.DailyForecastMapper
import cszsm.dolgok.forecast.data.mappers.HourlyForecastMapper
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ForecastRepositoryImpl(
    private val weatherDataSource: WeatherDataSource,
    private val hourlyForecastMapper: HourlyForecastMapper,
    private val dailyForecastMapper: DailyForecastMapper,
) : ForecastRepository {

    override val hourlyForecastStream = MutableStateFlow<LoadedData<HourlyForecast>?>(null)

    override val dailyForecastStream = MutableStateFlow<LoadedData<DailyForecast>?>(null)

    override suspend fun fetchHourlyForecast(
        latitude: Float,
        longitude: Float,
        timeInterval: DateTimeInterval,
    ) {
        hourlyForecastStream.update { LoadedData.Loading(data = it?.data) }

        try {
            val fetchedHourlyForecast = weatherDataSource.getHourlyForecast(
                latitude = latitude,
                longitude = longitude,
                startHour = timeInterval.start,
                endHour = timeInterval.end,
            ).let(hourlyForecastMapper::map)

            hourlyForecastStream.update {
                LoadedData.Success(
                    data = it?.data?.concatenate(fetchedHourlyForecast) ?: fetchedHourlyForecast
                )
            }
        } catch (e: Exception) {
            hourlyForecastStream.update {
                LoadedData.Failure(
                    data = it?.data,
                    error = DataError.NETWORK
                )
            }
        }
    }

    override suspend fun fetchDailyForecast(latitude: Float, longitude: Float) {
        dailyForecastStream.value = LoadedData.Loading()

        try {
            val fetchedDailyForecast = weatherDataSource.getDailyForecast(
                latitude = latitude,
                longitude = longitude,
            ).let(dailyForecastMapper::map)

            dailyForecastStream.value = LoadedData.Success(data = fetchedDailyForecast)
        } catch (e: Exception) {
            dailyForecastStream.value = LoadedData.Failure(error = DataError.NETWORK)
        }
    }

    private fun HourlyForecast.concatenate(other: HourlyForecast) =
        HourlyForecast(
            hours = this.hours + other.hours
        )
}