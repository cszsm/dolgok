package cszsm.dolgok.core.di

import cszsm.dolgok.core.domain.usecases.GetCurrentTimeUseCase
import kotlinx.datetime.Clock
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    single<Clock> { Clock.System }
    singleOf(::GetCurrentTimeUseCase)
}