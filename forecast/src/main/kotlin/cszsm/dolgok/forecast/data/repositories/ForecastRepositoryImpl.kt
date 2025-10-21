package cszsm.dolgok.forecast.data.repositories

import android.util.Log
import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.models.DateTimeInterval
import cszsm.dolgok.core.domain.models.FetchedData
import cszsm.dolgok.forecast.data.datasources.WeatherDataSource
import cszsm.dolgok.forecast.data.mappers.DailyForecastMapper
import cszsm.dolgok.forecast.data.mappers.HourlyForecastMapper
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.io.IOException

internal class ForecastRepositoryImpl(
    private val weatherDataSource: WeatherDataSource,
    private val hourlyForecastMapper: HourlyForecastMapper,
    private val dailyForecastMapper: DailyForecastMapper,
) : ForecastRepository {

    override val hourlyForecastStream = MutableStateFlow(FetchedData<HourlyForecast>())

    override val dailyForecastStream = MutableStateFlow(FetchedData<DailyForecast>())

    override suspend fun fetchHourlyForecast(
        latitude: Float,
        longitude: Float,
        timeInterval: DateTimeInterval,
    ) {
        hourlyForecastStream.update { it.copy(loading = true) }

        try {
            val newBatchOfHourlyForecast = weatherDataSource.getHourlyForecast(
                latitude = latitude,
                longitude = longitude,
                startHour = timeInterval.start,
                endHour = timeInterval.end,
            ).let(hourlyForecastMapper::map)

            hourlyForecastStream.update {
                it.copy(
                    data = it.data?.concatenate(newBatchOfHourlyForecast)
                        ?: newBatchOfHourlyForecast,
                    loading = false,
                )
            }
        } catch (e: IOException) {
            Log.e(TAG, e.message.toString())
            hourlyForecastStream.update {
                it.copy(
                    error = DataError.NETWORK,
                    loading = false,
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            hourlyForecastStream.update {
                it.copy(
                    error = DataError.UNKNOWN,
                    loading = false,
                )
            }
        }
    }

    override suspend fun fetchDailyForecast(latitude: Float, longitude: Float) {
        dailyForecastStream.update { it.copy(loading = true) }

        try {
            val dailyForecast = weatherDataSource.getDailyForecast(
                latitude = latitude,
                longitude = longitude,
            ).let(dailyForecastMapper::map)

            dailyForecastStream.update {
                it.copy(
                    data = dailyForecast,
                    loading = false,
                )
            }
        } catch (e: IOException) {
            Log.e(TAG, e.message.toString())
            dailyForecastStream.update {
                it.copy(
                    error = DataError.NETWORK,
                    loading = false,
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            dailyForecastStream.update {
                it.copy(
                    error = DataError.UNKNOWN,
                    loading = false,
                )
            }
        }
    }

    private fun HourlyForecast.concatenate(other: HourlyForecast) =
        HourlyForecast(
            hours = this.hours + other.hours
        )

    private companion object {
        val TAG = ForecastRepositoryImpl::class.simpleName
    }
}