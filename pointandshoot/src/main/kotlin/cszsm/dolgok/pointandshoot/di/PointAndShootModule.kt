package cszsm.dolgok.pointandshoot.di

import cszsm.dolgok.pointandshoot.presentation.viewmodels.PointAndShootViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val pointAndShootModule = module {
    viewModelOf(::PointAndShootViewModel)
}