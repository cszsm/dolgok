package cszsm.dolgok.animation.di

import cszsm.dolgok.animation.domain.usecases.GetComplexDataPart1UseCase
import cszsm.dolgok.animation.domain.usecases.GetComplexDataPart2UseCase
import cszsm.dolgok.animation.domain.usecases.GetSimpleDataUseCase
import cszsm.dolgok.animation.presentation.viewmodels.ProgressIndicatorViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val animationModule = module {
    singleOf(::GetSimpleDataUseCase)
    singleOf(::GetComplexDataPart1UseCase)
    singleOf(::GetComplexDataPart2UseCase)
    viewModelOf(::ProgressIndicatorViewModel)
}