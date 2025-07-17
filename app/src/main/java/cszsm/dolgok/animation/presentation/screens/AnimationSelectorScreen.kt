package cszsm.dolgok.animation.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnimationSelectorScreen(
    navigateToSimpleAnimation: () -> Unit,
    navigateToProgressIndicator: () -> Unit,
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
                onClick = navigateToSimpleAnimation,
            ) {
                Text("Simple animation")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = navigateToProgressIndicator,
            ) {
                Text("Progress indicator")
            }
        }
    }
}