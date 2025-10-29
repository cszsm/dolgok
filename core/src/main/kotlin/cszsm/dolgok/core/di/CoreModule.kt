package cszsm.dolgok.core.di

import cszsm.dolgok.core.domain.usecases.GetCurrentTimeUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val coreModule = module {
    single<Clock> { Clock.System }
    singleOf(::GetCurrentTimeUseCase)
}