package cszsm.dolgok.core.data.managers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

class PermissionManager(
    private val context: Context,
) {
    fun isLocationPermissionGranted(): Boolean =
        PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
}