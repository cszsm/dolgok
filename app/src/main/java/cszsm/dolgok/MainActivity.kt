package cszsm.dolgok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cszsm.dolgok.data.WeatherService
import cszsm.dolgok.ui.screens.lazycolumn.LazyColumnScreen
import cszsm.dolgok.ui.screens.main.MainScreen
import cszsm.dolgok.ui.screens.weather.ForecastScreen
import cszsm.dolgok.ui.theme.DolgokTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    private val weatherService = WeatherService.create()

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
                    composable<Forecast> { ForecastScreen(weatherService = weatherService) }
                }
            }
        }
    }
}

@Serializable
object Main

@Serializable
object LazyColumn

@Serializable
object Forecast