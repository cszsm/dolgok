package cszsm.dolgok.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cszsm.dolgok.localization.R

@Composable
internal fun HomeScreen(
    onNavigateToForecast: () -> Unit,
    onNavigateToAnimation: () -> Unit,
) {
    Scaffold { contentPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            Button(
                onClick = { onNavigateToForecast() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = stringResource(R.string.home_option_forecast))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onNavigateToAnimation() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = stringResource(R.string.home_option_animation))
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToForecast = {},
        onNavigateToAnimation = {},
    )
}