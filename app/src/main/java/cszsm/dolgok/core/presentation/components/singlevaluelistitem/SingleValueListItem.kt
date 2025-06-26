package cszsm.dolgok.core.presentation.components.singlevaluelistitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cszsm.dolgok.core.presentation.theme.Typography

@Composable
fun SingleValueListItem(
    title: String,
    value: String,
    shapeParams: SingleValueListItemShapeParams,
) {
    SingleValueListItem(
        title = title,
        value = value,
        shapes = shapeParams.toItemShapes(),
    )
}

@Composable
private fun SingleValueListItem(
    title: String,
    value: String,
    shapes: SingleValueListItemShapes,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Surface(
            shape = shapes.leadingShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.weight(6f),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
        Surface(
            shape = shapes.trailingShape,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.weight(4f),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = value,
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SingleValueListItem_Preview() {
    SingleValueListItem(
        title = "11:00",
        value = "1,1 mm",
        shapes = SingleValueListItemShapes.Single,
    )
}