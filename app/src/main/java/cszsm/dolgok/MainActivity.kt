package cszsm.dolgok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cszsm.dolgok.ui.screens.main.MainScreen
import cszsm.dolgok.ui.theme.DolgokTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DolgokTheme {
                MainScreen()
            }
        }
    }
}
