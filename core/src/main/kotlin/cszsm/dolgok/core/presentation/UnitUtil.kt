package cszsm.dolgok.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cszsm.dolgok.localization.R
import kotlinx.datetime.DayOfWeek

@Composable
fun DayOfWeek.displayName(): String =
    when (this) {
        DayOfWeek.MONDAY -> stringResource(R.string.core_week_monday)
        DayOfWeek.TUESDAY -> stringResource(R.string.core_week_tuesday)
        DayOfWeek.WEDNESDAY -> stringResource(R.string.core_week_wednesday)
        DayOfWeek.THURSDAY -> stringResource(R.string.core_week_thursday)
        DayOfWeek.FRIDAY -> stringResource(R.string.core_week_friday)
        DayOfWeek.SATURDAY -> stringResource(R.string.core_week_saturday)
        DayOfWeek.SUNDAY -> stringResource(R.string.core_week_sunday)
    }