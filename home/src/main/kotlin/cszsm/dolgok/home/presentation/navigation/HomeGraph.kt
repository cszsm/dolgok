package cszsm.dolgok.home.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import cszsm.dolgok.home.presentation.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object HomeGraphRoute

@Serializable
private object HomeRoute

fun NavGraphBuilder.addHomeGraph(
    navigateToForecast: () -> Unit,
    navigateToAnimation: () -> Unit,
    navigateToPointAndShoot: () -> Unit,
) {
    navigation<HomeGraphRoute>(
        startDestination = HomeRoute,
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToForecast = navigateToForecast,
                onNavigateToAnimation = navigateToAnimation,
                onNavigateToPointAndShoot = navigateToPointAndShoot,
            )
        }
    }
}