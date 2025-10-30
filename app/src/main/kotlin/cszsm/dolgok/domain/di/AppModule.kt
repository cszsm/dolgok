package cszsm.dolgok.domain.di

import cszsm.dolgok.animation.di.animationModule
import cszsm.dolgok.core.di.coreModule
import cszsm.dolgok.forecast.di.forecastModule
import cszsm.dolgok.pointandshoot.di.pointAndShootModule

val appModules = listOf(
    coreModule,
    forecastModule,
    animationModule,
    pointAndShootModule,
)
