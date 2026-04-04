package cszsm.dolgok.core.presentation.components.permission

import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun PermissionRequestDialog(
    permission: String,
    state: PermissionRequestState,
    onStatusChange: (PermissionStatus) -> Unit,
) {
    val activity = LocalActivity.current
    val context = LocalContext.current

    val permissionStatus =
        remember { mutableStateOf<PermissionStatus>(PermissionStatus.NotRequested) }
    LaunchedEffect(permissionStatus.value) { onStatusChange(permissionStatus.value) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            state.hideDialog()

            if (granted) {
                permissionStatus.value = PermissionStatus.Granted
            } else {
                val rationaleNeeded = activity?.shouldShowRequestPermissionRationale(permission)
                permissionStatus.value =
                    PermissionStatus.Denied(shouldShowRationale = rationaleNeeded == true)
            }
        }
    )

    LaunchedEffect(state.dialogShown.value) {
        if (state.dialogShown.value) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                permissionStatus.value = PermissionStatus.Granted
            } else {
                permissionLauncher.launch(permission)
            }

            state.hideDialog()
        }
    }
}

@Stable
class PermissionRequestState {
    internal val dialogShown = mutableStateOf(false)

    fun showDialog() {
        dialogShown.value = true
    }

    fun hideDialog() {
        dialogShown.value = false
    }
}

sealed interface PermissionStatus {
    data object NotRequested : PermissionStatus
    data object RequestInProgress : PermissionStatus
    data object Granted : PermissionStatus
    data class Denied(val shouldShowRationale: Boolean) : PermissionStatus
}