package cszsm.dolgok.pointandshoot.presentation.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.camera2.CameraMetadata
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import cszsm.dolgok.pointandshoot.presentation.states.PointAndShootScreenState
import cszsm.dolgok.pointandshoot.presentation.util.getEffectiveFocalLength
import cszsm.dolgok.pointandshoot.presentation.util.getMainCameraCharacteristics
import cszsm.dolgok.pointandshoot.presentation.util.setAeMode
import cszsm.dolgok.pointandshoot.presentation.util.setAfMode
import cszsm.dolgok.pointandshoot.presentation.util.setAwbMode
import cszsm.dolgok.pointandshoot.presentation.util.setExposureTime
import cszsm.dolgok.pointandshoot.presentation.util.setFocusDistance
import cszsm.dolgok.pointandshoot.presentation.util.setIso
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class PointAndShootViewModel : ViewModel() {

    private val _state = MutableStateFlow(PointAndShootScreenState())
    val state: StateFlow<PointAndShootScreenState> = _state.asStateFlow()

    // TODO: probably need to migrate to Camera2 instead for low-level camera control, as
    //  Camera2Interop doesn't seem to be reliable
    private var camera: Camera? = null
    private var fullFrameZoom: Float? = null

    private val cameraPreviewUseCase = createPreviewUseCase()
    private val cameraCaptureUseCase = createCaptureUseCase()

    suspend fun bindToCamera(applicationContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(applicationContext)
        camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner = lifecycleOwner,
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
            useCases = arrayOf(
                cameraPreviewUseCase,
                cameraCaptureUseCase,
            ),
        )
        fullFrameZoom = calculateFullFrameZoom(applicationContext)

        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
        }
    }

    fun takePicture(applicationContext: Context) {
        cameraCaptureUseCase.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    _state.update {
                        it.copy(
                            photo = image.toRotatedBitmap()
                        )
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e(TAG, "couldn't take photo: $exception")
                }
            }
        )
    }

    fun changeZoom() {
        if (state.value.zoomedToFullFrame) {
            camera?.cameraControl?.setZoomRatio(1f)
            _state.update { it.copy(zoomedToFullFrame = false) }
        } else {
            fullFrameZoom?.let { zoom ->
                camera?.cameraControl?.setZoomRatio(zoom)
                _state.update { it.copy(zoomedToFullFrame = true) }
            }
        }
    }

    fun deletePicture() {
        _state.update { it.copy(photo = null) }
    }

    @OptIn(ExperimentalCamera2Interop::class)
    private fun createPreviewUseCase(): Preview {
        val builder = Preview.Builder()
            .setResolutionSelector(
                ResolutionSelector.Builder()
                    .setAspectRatioStrategy(
                        AspectRatioStrategy(
                            AspectRatio.RATIO_4_3,
                            AspectRatioStrategy.FALLBACK_RULE_AUTO
                        )
                    ).build()
            )
        Camera2Interop.Extender(builder).apply {
            setAwbMode(CameraMetadata.CONTROL_AWB_MODE_OFF)
            setAfMode(CameraMetadata.CONTROL_AF_MODE_OFF)
            setAeMode(CameraMetadata.CONTROL_AE_MODE_OFF)
            setFocusDistance(1f) // infinity focus
            setExposureTime(10_000_000L) // 10 ms
            setIso(200)
        }
        return builder
            .build()
            .apply {
                setSurfaceProvider { newSurfaceRequest ->
                    _state.update { it.copy(surfaceRequest = newSurfaceRequest) }
                }
            }
    }

    @OptIn(ExperimentalCamera2Interop::class)
    private fun createCaptureUseCase(): ImageCapture {
        val builder = ImageCapture.Builder()
        Camera2Interop.Extender(builder).apply {
            setAwbMode(CameraMetadata.CONTROL_AWB_MODE_OFF)
            setAfMode(CameraMetadata.CONTROL_AF_MODE_OFF)
            setAeMode(CameraMetadata.CONTROL_AE_MODE_OFF)
            setFocusDistance(1f) // infinity focus
            // TODO: this exposure time setting sometimes has no effect (it doesn't override the preview)
            setExposureTime(100_000_000L) // 100 ms
            setIso(200)
            // TODO: flash doesn't work as expected, check again when Camera2Interop is not experimental anymore
            //  setFlashMode(CameraMetadata.FLASH_MODE_SINGLE)
        }
        return builder.build()
    }

    private fun ImageProxy.toRotatedBitmap(): Bitmap {
        val matrix = Matrix().apply {
            postRotate(imageInfo.rotationDegrees.toFloat())
        }
        return Bitmap.createBitmap(
            toBitmap(),
            0,
            0,
            width,
            height,
            matrix,
            true
        )
    }

    private fun calculateFullFrameZoom(applicationContext: Context): Float? {
        val characteristics = applicationContext.getMainCameraCharacteristics() ?: return null
        val effectiveFocalLength = characteristics.getEffectiveFocalLength() ?: return null
        return FULL_FRAME_LENGTH / effectiveFocalLength
    }

    private companion object {
        val TAG = PointAndShootViewModel::class.simpleName
        const val FULL_FRAME_LENGTH = 35
    }
}