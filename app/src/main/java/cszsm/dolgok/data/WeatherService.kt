package cszsm.dolgok.data

import cszsm.dolgok.data.dto.ForecastResponse

interface WeatherService {

    suspend fun getForecast(): ForecastResponse?
}