package cszsm.dolgok.ui.screens.weather

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
import cszsm.dolgok.data.WeatherService
import cszsm.dolgok.data.dto.ForecastResponse
import kotlinx.coroutines.launch

@Composable
fun ForecastScreen(
    weatherService: WeatherService,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val scope = rememberCoroutineScope()
        var forecast by remember { mutableStateOf<ForecastResponse?>(null) }

        LaunchedEffect(true) {
            scope.launch {
                forecast = try {
                    weatherService.getForecast()
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
                forecast?.hourly?.time?.forEachIndexed { index, time ->
                    Text("$time - ${forecast?.hourly?.temperature_2m?.getOrNull(index)}")
                }
            }
        }
    }
}