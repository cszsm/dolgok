package cszsm.dolgok.pointandshoot.presentation.states

import android.graphics.Bitmap
import androidx.camera.core.SurfaceRequest

data class PointAndShootScreenState(
    val photo: Bitmap? = null,
    val zoomedToFullFrame: Boolean = false,
    val surfaceRequest: SurfaceRequest? = null,
)