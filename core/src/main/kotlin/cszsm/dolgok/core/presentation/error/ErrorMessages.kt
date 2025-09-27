package cszsm.dolgok.core.presentation.error

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cszsm.dolgok.core.domain.error.DataError
import cszsm.dolgok.localization.R

@Composable
fun DataError.getMessage() =
    when (this) {
        DataError.INCOMPLETE_DATA -> stringResource(R.string.core_error_incomplete_data)
        DataError.WRONG_DATA_FORMAT -> stringResource(R.string.core_error_wrong_data_format)
        DataError.NETWORK -> stringResource(R.string.core_error_network)
    }