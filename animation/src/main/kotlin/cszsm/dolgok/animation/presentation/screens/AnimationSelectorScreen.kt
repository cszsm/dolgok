package cszsm.dolgok.animation.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cszsm.dolgok.localization.R

@Composable
internal fun AnimationSelectorScreen(
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
                onClick = navigateToProgressIndicator,
            ) {
                Text(stringResource(R.string.animation_progress_indicator))
            }
        }
    }
}