package cszsm.dolgok.domain.transformers

import cszsm.dolgok.data.dto.ForecastApiModel
import cszsm.dolgok.data.dto.ForecastDataApiModel
import cszsm.dolgok.domain.dto.Forecast
import cszsm.dolgok.domain.dto.ForecastUnit
import kotlinx.datetime.LocalDateTime

class ForecastTransformer {

    fun transform(response: ForecastApiModel?): Forecast? {
        response ?: return null

        return Forecast(
            hourly = response.hourly.transform(),
        )
    }

    private fun ForecastDataApiModel.transform() =
        time.mapIndexed { index, time ->
            ForecastUnit(
                time = time.toLocalDateTime(),
                temperature = temperature_2m.getOrNull(index),
                rain = rain.getOrNull(index),
                pressure = surface_pressure.getOrNull(index),
            )
        }

    private fun String.toLocalDateTime() =
        try {
            LocalDateTime.parse(this)
        } catch (e: IllegalArgumentException) {
            null
        }
}