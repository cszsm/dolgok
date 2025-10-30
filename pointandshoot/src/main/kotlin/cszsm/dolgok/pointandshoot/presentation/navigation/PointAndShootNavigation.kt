package cszsm.dolgok.pointandshoot.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import cszsm.dolgok.pointandshoot.presentation.screens.PointAndShootScreen
import kotlinx.serialization.Serializable

@Serializable
object PointAndShootGraphRoute

@Serializable
private object PointAndShootRoute

fun NavGraphBuilder.addPointAndShootGraph(
) {
    navigation<PointAndShootGraphRoute>(
        startDestination = PointAndShootRoute,
    ) {
        composable<PointAndShootRoute> { PointAndShootScreen() }
    }
}