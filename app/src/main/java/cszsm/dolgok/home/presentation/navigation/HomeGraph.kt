package cszsm.dolgok.home.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import cszsm.dolgok.animation.presentation.navigation.AnimationGraphRoute
import cszsm.dolgok.forecast.presentation.navigation.ForecastGraphRoute
import cszsm.dolgok.home.presentation.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object HomeGraphRoute

@Serializable
private object HomeRoute

fun NavGraphBuilder.addHomeGraph(
    navHostController: NavHostController,
) {
    navigation<HomeGraphRoute>(
        startDestination = HomeRoute,
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToForecast = {
                    navHostController.navigate(route = ForecastGraphRoute)
                },
                onNavigateToAnimation = {
                    navHostController.navigate(route = AnimationGraphRoute)
                }
            )
        }
    }
}