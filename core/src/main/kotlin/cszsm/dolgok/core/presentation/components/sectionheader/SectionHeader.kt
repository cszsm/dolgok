package cszsm.dolgok.core.presentation.components.sectionheader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cszsm.dolgok.core.presentation.theme.Typography

@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = Typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
        )
    }
}

@Preview
@Composable
private fun SectionHeader_Preview() {
    SectionHeader(text = "tuesday")
}