package cszsm.dolgok.forecast.data.transformers

import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.core.domain.result.Result
import cszsm.dolgok.forecast.data.models.ForecastApiModel
import cszsm.dolgok.forecast.data.models.ForecastDataApiModel
import cszsm.dolgok.forecast.domain.models.DailyForecast
import cszsm.dolgok.forecast.domain.models.DailyForecastUnit
import cszsm.dolgok.forecast.domain.models.HourlyForecast
import cszsm.dolgok.forecast.domain.models.HourlyForecastUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class ForecastTransformer {

    fun transformHourly(response: ForecastApiModel?): Result<HourlyForecast, DataError> {
        response?.hourly ?: return Result.Failure(error = DataError.INCOMPLETE_DATA)

        return response.hourly.transformHourly()
    }

    fun transformDaily(response: ForecastApiModel?): Result<DailyForecast, DataError> {
        response?.daily ?: return Result.Failure(error = DataError.INCOMPLETE_DATA)

        return response.daily.transformDaily()
    }

    private fun ForecastDataApiModel.transformHourly(): Result<HourlyForecast, DataError> {

        val hours = time.mapIndexed { index, time ->

            val parsedTime = time.toLocalDateTimeOrNull()
                ?: return Result.Failure(DataError.WRONG_DATA_FORMAT)
            val parsedTemperature = temperature_2m?.getOrNull(index)
                ?: return Result.Failure(DataError.INCOMPLETE_DATA)
            val parsedRain = rain?.getOrNull(index)
                ?: return Result.Failure(DataError.INCOMPLETE_DATA)
            val parsedPressure = surface_pressure?.getOrNull(index)
                ?: return Result.Failure(DataError.INCOMPLETE_DATA)

            HourlyForecastUnit(
                time = parsedTime,
                temperature = parsedTemperature,
                rain = parsedRain,
                pressure = parsedPressure,
            )
        }

        return Result.Success(HourlyForecast(hours = hours))
    }

    private fun ForecastDataApiModel.transformDaily(): Result<DailyForecast, DataError> {

        val days = time.mapIndexed { index, time ->

            val parsedDate = time.toLocalDateOrNull()
                ?: return Result.Failure(DataError.WRONG_DATA_FORMAT)
            val parsedTemperatureMax = temperature_2m_max?.getOrNull(index)
                ?: return Result.Failure(DataError.INCOMPLETE_DATA)
            val parsedTemperatureMin = temperature_2m_min?.getOrNull(index)
                ?: return Result.Failure(DataError.INCOMPLETE_DATA)
            val parsedRainSum = rain_sum?.getOrNull(index)
                ?: return Result.Failure(DataError.INCOMPLETE_DATA)

            DailyForecastUnit(
                date = parsedDate,
                temperatureMax = parsedTemperatureMax,
                temperatureMin = parsedTemperatureMin,
                rainSum = parsedRainSum,
            )
        }

        return Result.Success(DailyForecast(days = days))
    }

    private fun String.toLocalDateTimeOrNull() =
        try {
            LocalDateTime.parse(this)
        } catch (e: IllegalArgumentException) {
            null
        }

    private fun String.toLocalDateOrNull() =
        try {
            LocalDate.parse(this)
        } catch (e: IllegalArgumentException) {
            null
        }
}