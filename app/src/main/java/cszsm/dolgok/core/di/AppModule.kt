package cszsm.dolgok.core.di

import cszsm.dolgok.forecast.data.ForecastRepository
import cszsm.dolgok.forecast.data.ForecastTransformer
import cszsm.dolgok.forecast.data.WeatherDataSource
import cszsm.dolgok.forecast.data.WeatherDataSourceImpl
import cszsm.dolgok.forecast.domain.usecases.CalculateForecastDayIntervalUseCase
import cszsm.dolgok.forecast.domain.usecases.GetDailyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.GetHourlyForecastUseCase
import cszsm.dolgok.forecast.presentation.ForecastViewModel
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private const val WEATHER_BASE_URL = "api.open-meteo.com/v1/"

val appModule = module {
    single<WeatherDataSource> {
        WeatherDataSourceImpl(
            client = HttpClient(OkHttp) {
                common()

                defaultRequest {
                    host = WEATHER_BASE_URL
                    url { protocol = URLProtocol.HTTPS }
                }
            }
        )
    }
    singleOf(::ForecastRepository)
    singleOf(::ForecastTransformer)
    singleOf(::GetHourlyForecastUseCase)
    singleOf(::GetDailyForecastUseCase)
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

    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.INFO
    }

    expectSuccess = true
}