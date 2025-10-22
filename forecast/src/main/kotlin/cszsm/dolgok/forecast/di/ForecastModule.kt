package cszsm.dolgok.forecast.di

import cszsm.dolgok.forecast.data.datasources.WeatherDataSource
import cszsm.dolgok.forecast.data.datasources.WeatherDataSourceImpl
import cszsm.dolgok.forecast.data.datasources.mock.MockWeatherDataSource
import cszsm.dolgok.forecast.data.mappers.DailyForecastMapper
import cszsm.dolgok.forecast.data.mappers.HourlyForecastMapper
import cszsm.dolgok.forecast.data.repositories.ForecastRepositoryImpl
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
import cszsm.dolgok.forecast.domain.usecases.CalculateDailyForecastIntervalUseCase
import cszsm.dolgok.forecast.domain.usecases.CalculateNextDayIntervalUseCase
import cszsm.dolgok.forecast.domain.usecases.FetchDailyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.FetchFirstDayHourlyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.FetchMoreHourlyForecastUseCase
import cszsm.dolgok.forecast.domain.usecases.IsMoreHourlyForecastAllowedUseCase
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

@Suppress("SpellCheckingInspection")
private const val WEATHER_BASE_URL = "api.open-meteo.com/v1/"

private const val MOCK_DATA = false

val forecastModule = module {
    single<WeatherDataSource> {
        // TODO: do something more sophisticated, like depend on build environment
        if (MOCK_DATA) {
            MockWeatherDataSource()
        } else {
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
    }
    single<ForecastRepository> { ForecastRepositoryImpl(get(), get(), get()) }
    singleOf(::HourlyForecastMapper)
    singleOf(::DailyForecastMapper)
    singleOf(::FetchFirstDayHourlyForecastUseCase)
    singleOf(::FetchMoreHourlyForecastUseCase)
    singleOf(::FetchDailyForecastUseCase)
    singleOf(::CalculateNextDayIntervalUseCase)
    singleOf(::CalculateDailyForecastIntervalUseCase)
    singleOf(::IsMoreHourlyForecastAllowedUseCase)
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