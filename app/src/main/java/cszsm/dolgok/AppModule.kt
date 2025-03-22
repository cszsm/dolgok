package cszsm.dolgok

import cszsm.dolgok.data.WeatherService
import cszsm.dolgok.data.WeatherServiceImpl
import cszsm.dolgok.domain.transformers.ForecastTransformer
import cszsm.dolgok.domain.usecases.GetForecastUseCase
import cszsm.dolgok.ui.screens.forecast.ForecastViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<WeatherService> {
        WeatherServiceImpl(
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
    singleOf(::ForecastTransformer)
    singleOf(::GetForecastUseCase)
    viewModelOf(::ForecastViewModel)
}