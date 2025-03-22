package cszsm.dolgok.ui.screens.forecast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    viewModel.loadForecast()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        if (state.forecast == null) {
            Text(text = "Loading")
        } else {
            Column {
                state.forecast?.hourly?.forEach {
                    val time = it.time?.time.toString()
                    val temperature = it.temperature
                    Text("$time - $temperature")
                }
            }
        }
    }
}