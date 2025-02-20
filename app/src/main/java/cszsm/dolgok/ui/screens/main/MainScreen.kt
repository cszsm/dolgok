package cszsm.dolgok.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen(
    onNavigateToLazyColumn: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Button(
            onClick = { onNavigateToLazyColumn() },
            modifier = Modifier.wrapContentSize()
        ) {
            Text(text = "LazyColumn")
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(onNavigateToLazyColumn = {})
}