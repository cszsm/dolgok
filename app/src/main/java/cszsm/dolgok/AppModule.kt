package cszsm.dolgok

import cszsm.dolgok.data.WeatherService
import cszsm.dolgok.data.WeatherServiceImpl
import cszsm.dolgok.domain.transformers.ForecastTransformer
import cszsm.dolgok.domain.usecases.CalculateForecastDayIntervalUseCase
import cszsm.dolgok.domain.usecases.GetForecastUseCase
import cszsm.dolgok.ui.screens.forecast.ForecastViewModel
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private const val WEATHER_BASE_URL = "api.open-meteo.com/v1/"

val appModule = module {
    single<WeatherService> {
        WeatherServiceImpl(
            client = HttpClient(OkHttp) {
                common()

                defaultRequest {
                    host = WEATHER_BASE_URL
                    url { protocol = URLProtocol.HTTPS }
                }
            }
        )
    }
    singleOf(::ForecastTransformer)
    singleOf(::GetForecastUseCase)
    singleOf(::CalculateForecastDayIntervalUseCase)
    viewModelOf(::ForecastViewModel)
}

private fun <T : HttpClientEngineConfig> (HttpClientConfig<T>).common() {
    install(Resources)

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