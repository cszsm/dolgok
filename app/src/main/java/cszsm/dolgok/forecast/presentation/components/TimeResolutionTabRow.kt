package cszsm.dolgok.forecast.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cszsm.dolgok.forecast.presentation.TimeResolution

@Composable
fun TimeResolutionTabRow(
    timeResolutions: List<TimeResolution>,
    selectedTimeResolution: TimeResolution,
    onSelect: (TimeResolution) -> Unit,
) {
    val selectedTabIndex = selectedTimeResolution.ordinal

    PrimaryTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        timeResolutions.forEachIndexed { index, timeResolution ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onSelect(TimeResolution.entries[index]) },
                modifier = Modifier
                    .width(100.dp)
                    .height(48.dp)
            ) {
                Box(
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = timeResolution.label,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }
    }
}

private val TimeResolution.label
    get() = when (this) {
        TimeResolution.HOURLY -> "Hourly"
        TimeResolution.DAILY -> "Daily"
    }
