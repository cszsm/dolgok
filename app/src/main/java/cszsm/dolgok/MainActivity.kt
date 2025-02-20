package cszsm.dolgok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cszsm.dolgok.ui.screens.lazycolumn.LazyColumnScreen
import cszsm.dolgok.ui.screens.main.MainScreen
import cszsm.dolgok.ui.theme.DolgokTheme
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
                        MainScreen(onNavigateToLazyColumn = {
                            navController.navigate(
                                route = LazyColumn
                            )
                        })
                    }
                    composable<LazyColumn> { LazyColumnScreen() }
                }
            }
        }
    }
}

@Serializable
object Main

@Serializable
object LazyColumn