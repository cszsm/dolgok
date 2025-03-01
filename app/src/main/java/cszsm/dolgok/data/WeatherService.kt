package cszsm.dolgok.data

import cszsm.dolgok.data.dto.ForecastResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface WeatherService {

    suspend fun getForecast(): ForecastResponse?

    companion object {
        fun create(): WeatherService = WeatherServiceImpl(
            client = HttpClient(OkHttp) {
                install(ContentNegotiation) {
                    json(
                        json = Json {
                            encodeDefaults = true
                            isLenient = true
                            allowSpecialFloatingPointValues = true
                            allowStructuredMapKeys = true
                            prettyPrint = false
                            useArrayPolymorphism = false

                            ignoreUnknownKeys = true
                        }
                    )
                }

                expectSuccess = true
            }
        )
    }
}