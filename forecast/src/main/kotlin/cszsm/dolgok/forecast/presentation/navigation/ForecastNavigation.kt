package cszsm.dolgok.forecast.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import cszsm.dolgok.forecast.presentation.screens.ForecastScreen
import kotlinx.serialization.Serializable

@Serializable
object ForecastGraphRoute

@Serializable
private object ForecastRoute

fun NavGraphBuilder.addForecastGraph() {
    navigation<ForecastGraphRoute>(
        startDestination = ForecastRoute,
    ) {
        composable<ForecastRoute> { ForecastScreen() }
    }
}