package cszsm.dolgok.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cszsm.dolgok.animation.presentation.navigation.AnimationGraphRoute
import cszsm.dolgok.animation.presentation.navigation.addAnimationGraph
import cszsm.dolgok.core.presentation.theme.DolgokTheme
import cszsm.dolgok.forecast.presentation.navigation.ForecastGraphRoute
import cszsm.dolgok.forecast.presentation.navigation.addForecastGraph
import cszsm.dolgok.home.presentation.navigation.HomeGraphRoute
import cszsm.dolgok.home.presentation.navigation.addHomeGraph

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DolgokTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = HomeGraphRoute,
                ) {
                    addHomeGraph(
                        navigateToForecast = { navController.navigate(route = ForecastGraphRoute) },
                        navigateToAnimation = { navController.navigate(route = AnimationGraphRoute) },
                    )
                    addForecastGraph()
                    addAnimationGraph(navHostController = navController)
                }
            }
        }
    }
}