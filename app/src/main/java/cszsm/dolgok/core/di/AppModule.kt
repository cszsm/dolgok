package cszsm.dolgok.core.di

import cszsm.dolgok.animation.domain.usecases.GetComplexDataPart1UseCase
import cszsm.dolgok.animation.domain.usecases.GetComplexDataPart2UseCase
import cszsm.dolgok.animation.domain.usecases.GetSimpleDataUseCase
import cszsm.dolgok.animation.presentation.viewmodels.ProgressIndicatorViewModel
import cszsm.dolgok.core.domain.usecases.GetCurrentTimeUseCase
import cszsm.dolgok.forecast.data.datasources.WeatherDataSource
import cszsm.dolgok.forecast.data.datasources.WeatherDataSourceImpl
import cszsm.dolgok.forecast.data.repositories.ForecastRepositoryImpl
import cszsm.dolgok.forecast.data.transformers.ForecastTransformer
import cszsm.dolgok.forecast.domain.repositories.ForecastRepository
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
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@Suppress("SpellCheckingInspection")
private const val WEATHER_BASE_URL = "api.open-meteo.com/v1/"

val appModule = module {
    single<Clock> { Clock.System }
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
    singleOf(::GetCurrentTimeUseCase)
    single<ForecastRepository> { ForecastRepositoryImpl(get(), get()) }
    singleOf(::ForecastTransformer)
    singleOf(::GetHourlyForecastUseCase)
    singleOf(::GetDailyForecastUseCase)
    singleOf(::CalculateForecastDayIntervalUseCase)
    singleOf(::GetSimpleDataUseCase)
    singleOf(::GetComplexDataPart1UseCase)
    singleOf(::GetComplexDataPart2UseCase)
    viewModelOf(::ForecastViewModel)
    viewModelOf(::ProgressIndicatorViewModel)
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