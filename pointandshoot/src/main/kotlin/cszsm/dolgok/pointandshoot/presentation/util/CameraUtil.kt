package cszsm.dolgok.pointandshoot.presentation.util

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import kotlin.math.sqrt

private const val CROP_FACTOR = 43.27f

internal fun Context.getMainCameraCharacteristics(): CameraCharacteristics? {
    val cameraManager = getSystemService(Context.CAMERA_SERVICE) as? CameraManager
        ?: return null
    // TODO: here I assumed one at 0 is always the main camera; needs check
    val cameraId = cameraManager.cameraIdList.getOrNull(0) ?: return null
    return cameraManager.getCameraCharacteristics(cameraId)
}

internal fun CameraCharacteristics.getEffectiveFocalLength(): Float? {
    val focalLength = getFocalLength() ?: return null
    val sensorDiameter = getSensorDiameter() ?: return null
    return focalLength * (CROP_FACTOR / sensorDiameter)
}

@ExperimentalCamera2Interop
internal fun Camera2Interop.Extender<out Any>.setAwbMode(awbMode: Int) {
    setCaptureRequestOption(CaptureRequest.CONTROL_AWB_MODE, awbMode)
}

@ExperimentalCamera2Interop
internal fun Camera2Interop.Extender<out Any>.setAfMode(afMode: Int) {
    setCaptureRequestOption(CaptureRequest.CONTROL_AF_MODE, afMode)
}

@ExperimentalCamera2Interop
internal fun Camera2Interop.Extender<out Any>.setAeMode(aeMode: Int) {
    setCaptureRequestOption(CaptureRequest.CONTROL_AE_MODE, aeMode)
}

@ExperimentalCamera2Interop
internal fun Camera2Interop.Extender<out Any>.setFocusDistance(focusDistance: Float) {
    setCaptureRequestOption(CaptureRequest.LENS_FOCUS_DISTANCE, focusDistance)
}

@ExperimentalCamera2Interop
internal fun Camera2Interop.Extender<out Any>.setExposureTime(exposureTime: Long) {
    setCaptureRequestOption(CaptureRequest.SENSOR_EXPOSURE_TIME, exposureTime)
}

@ExperimentalCamera2Interop
internal fun Camera2Interop.Extender<out Any>.setFrameDuration(frameDuration: Long) {
    setCaptureRequestOption(CaptureRequest.SENSOR_FRAME_DURATION, frameDuration)
}

@ExperimentalCamera2Interop
internal fun Camera2Interop.Extender<out Any>.setIso(iso: Int) {
    setCaptureRequestOption(CaptureRequest.SENSOR_SENSITIVITY, iso)
}

@ExperimentalCamera2Interop
internal fun Camera2Interop.Extender<out Any>.setFlashMode(flashMode: Int) {
    setCaptureRequestOption(CaptureRequest.FLASH_MODE, flashMode)
}

private fun CameraCharacteristics.getFocalLength(): Float? =
    get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)?.firstOrNull()

private fun CameraCharacteristics.getSensorDiameter(): Float? {
    val sensorSize = get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE) ?: return null
    return sqrt(sensorSize.height * sensorSize.height + sensorSize.width * sensorSize.width)
}