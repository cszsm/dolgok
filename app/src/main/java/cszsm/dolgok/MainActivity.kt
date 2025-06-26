package cszsm.dolgok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cszsm.dolgok.forecast.presentation.ForecastScreen
import cszsm.dolgok.lazycolumn.presentation.LazyColumnScreen
import cszsm.dolgok.home.presentation.MainScreen
import cszsm.dolgok.core.presentation.theme.DolgokTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DolgokTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Main,
                ) {
                    composable<Main> {
                        MainScreen(
                            onNavigateToLazyColumn = {
                                navController.navigate(
                                    route = LazyColumn
                                )
                            },
                            onNavigateToForecast = {
                                navController.navigate(
                                    route = Forecast
                                )
                            })
                    }
                    composable<LazyColumn> { LazyColumnScreen() }
                    composable<Forecast> { ForecastScreen() }
                }
            }
        }
    }
}

@Serializable
private object Main

@Serializable
private object LazyColumn

@Serializable
private object Forecast