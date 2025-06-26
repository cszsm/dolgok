package cszsm.dolgok.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onNavigateToLazyColumn: () -> Unit,
    onNavigateToForecast: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = { onNavigateToLazyColumn() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "LazyColumn")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onNavigateToForecast() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "Forecast")
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        onNavigateToLazyColumn = {},
        onNavigateToForecast = {},
    )
}