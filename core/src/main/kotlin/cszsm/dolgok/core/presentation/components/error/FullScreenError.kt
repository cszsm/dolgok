package cszsm.dolgok.core.presentation.components.error

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cszsm.dolgok.core.presentation.theme.Typography

@Composable
fun FullScreenError(
    message: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = message,
            style = Typography.headlineSmall,
        )
    }
}

@Preview
@Composable
private fun FullScreenError_Preview() {
    FullScreenError(
        message = "An error has happened."
    )
}
