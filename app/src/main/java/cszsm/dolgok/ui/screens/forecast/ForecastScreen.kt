package cszsm.dolgok.ui.screens.forecast

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cszsm.dolgok.domain.dto.Forecast
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = koinViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val scope = rememberCoroutineScope()
        var forecast by remember { mutableStateOf<Forecast?>(null) }

        LaunchedEffect(true) {
            scope.launch {
                forecast = try {
                    viewModel.getForecast()
                } catch (e: Exception) {
                    Log.e("ForecastScreen", e.localizedMessage ?: "unknown error")
                    null
                }
            }
        }

        if (forecast == null) {
            Text(text = "Loading")
        } else {
            Column {
                forecast?.hourly?.forEach {
                    val time = it.time?.time.toString()
                    val temperature = it.temperature
                    Text("$time - $temperature")
                }
            }
        }
    }
}