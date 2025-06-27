package cszsm.dolgok.core.presentation.components.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cszsm.dolgok.core.presentation.theme.Typography

@Preview
@Composable
fun FullScreenLoading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Loading...",
            style = Typography.headlineMedium,
        )
    }
}
