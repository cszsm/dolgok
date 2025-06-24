package cszsm.dolgok.domain.transformers

import cszsm.dolgok.data.dto.ForecastApiModel
import cszsm.dolgok.data.dto.ForecastDataApiModel
import cszsm.dolgok.domain.dto.DailyForecast
import cszsm.dolgok.domain.dto.DailyForecastUnit
import cszsm.dolgok.domain.dto.HourlyForecastUnit
import cszsm.dolgok.domain.dto.HourlyForecast
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class ForecastTransformer {

    fun transformHourly(response: ForecastApiModel?): HourlyForecast? {
        response ?: return null

        return HourlyForecast(
            hours = response.hourly?.transformHourly(),
        )
    }

    fun transformDaily(response: ForecastApiModel?): DailyForecast? {
        response ?: return null

        return DailyForecast(
            days = response.daily?.transformDaily()
        )
    }

    private fun ForecastDataApiModel.transformHourly() =
        time.mapIndexed { index, time ->
            HourlyForecastUnit(
                time = time.toLocalDateTime(),
                temperature = temperature_2m?.getOrNull(index),
                rain = rain?.getOrNull(index),
                pressure = surface_pressure?.getOrNull(index),
            )
        }

    private fun ForecastDataApiModel.transformDaily() =
        time.mapIndexed { index, time ->
            DailyForecastUnit(
                time = time.toLocalDate(),
                temperatureMax = temperature_2m_max?.getOrNull(index),
                temperatureMin = temperature_2m_min?.getOrNull(index),
                rainSum = rain_sum?.getOrNull(index),
            )
        }

    private fun String.toLocalDateTime() =
        try {
            LocalDateTime.parse(this)
        } catch (e: IllegalArgumentException) {
            null
        }

    private fun String.toLocalDate() =
        try {
            LocalDate.parse(this)
        } catch (e: IllegalArgumentException) {
            null
        }
}