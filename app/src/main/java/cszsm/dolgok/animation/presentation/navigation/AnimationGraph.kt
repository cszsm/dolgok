package cszsm.dolgok.animation.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import cszsm.dolgok.animation.presentation.screens.AnimationSelectorScreen
import cszsm.dolgok.animation.presentation.screens.ProgressIndicatorScreen
import cszsm.dolgok.animation.presentation.screens.SimpleAnimationScreen
import kotlinx.serialization.Serializable

@Serializable
object AnimationGraphRoute

@Serializable
private object AnimationSelectorRoute

@Serializable
private object SimpleAnimationRoute

@Serializable
private object ProgressIndicatorRoute

fun NavGraphBuilder.addAnimationGraph(
    navHostController: NavHostController,
) {
    navigation<AnimationGraphRoute>(
        startDestination = AnimationSelectorRoute
    ) {
        composable<AnimationSelectorRoute> {
            AnimationSelectorScreen(
                navigateToSimpleAnimation = {
                    navHostController.navigate(route = SimpleAnimationRoute)
                },
                navigateToProgressIndicator = {
                    navHostController.navigate(route = ProgressIndicatorRoute)
                },
            )
        }
        composable<SimpleAnimationRoute> { SimpleAnimationScreen() }
        composable<ProgressIndicatorRoute> { ProgressIndicatorScreen() }
    }
}