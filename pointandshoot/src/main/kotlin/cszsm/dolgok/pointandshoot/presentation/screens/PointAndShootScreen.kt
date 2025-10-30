package cszsm.dolgok.pointandshoot.presentation.screens

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import cszsm.dolgok.pointandshoot.presentation.viewmodels.PointAndShootViewModel
import org.koin.androidx.compose.koinViewModel
import cszsm.dolgok.core.R as coreR
import cszsm.dolgok.localization.R as localizationR

private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun PointAndShootScreen(
    viewModel: PointAndShootViewModel = koinViewModel(),
) {
    val cameraPermissionState = rememberPermissionState(CAMERA_PERMISSION)
    if (cameraPermissionState.status.isGranted) {
        CameraPreviewContent(viewModel = viewModel)
    } else {
        CameraPermissionRequestContent(permissionState = cameraPermissionState)
    }
}

@Composable
private fun CameraPreviewContent(
    viewModel: PointAndShootViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        viewModel.bindToCamera(applicationContext = context, lifecycleOwner = lifecycleOwner)
    }

    BackHandler(state.photo != null) {
        viewModel.deletePicture()
    }

    state.surfaceRequest?.let { request ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Black,
        ) { contentPadding ->

            val photoImageBitmap = state.photo?.asImageBitmap()

            AnimatedVisibility(
                visible = photoImageBitmap == null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                CameraView(
                    surfaceRequest = request,
                    modifier = Modifier.padding(contentPadding),
                    zoomed = state.zoomedToFullFrame,
                    takePicture = { viewModel.takePicture(applicationContext = context) },
                    changeZoom = { viewModel.changeZoom() },
                )
            }
            AnimatedVisibility(
                visible = photoImageBitmap != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                photoImageBitmap?.let {
                    PhotoView(bitmap = it)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CameraPermissionRequestContent(
    permissionState: PermissionState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
            .widthIn(max = 480.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val textToShow = if (permissionState.status.shouldShowRationale) {
            "rationale"
        } else {
            "first explanation"
        }
        Text(textToShow, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // TODO: handle permanent deny
            permissionState.launchPermissionRequest()
        }) {
            Text("request permission")
        }
    }
}

@Composable
fun PhotoView(
    modifier: Modifier = Modifier,
    bitmap: ImageBitmap,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = stringResource(localizationR.string.pointandshoot_accessibility_image_preview),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun CameraView(
    surfaceRequest: SurfaceRequest,
    modifier: Modifier = Modifier,
    zoomed: Boolean,
    takePicture: () -> Unit,
    changeZoom: () -> Unit,
) {
    Column(modifier = modifier) {
        CameraXViewfinder(
            surfaceRequest = surfaceRequest,
            modifier = Modifier
                .aspectRatio(ratio = 0.75f),
        )

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (shutter, zoom) = createRefs()

            IconButton(
                modifier = Modifier
                    .size(96.dp)
                    .constrainAs(shutter) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                onClick = { takePicture() }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(coreR.drawable.camera_48dp),
                    tint = Color.White,
                    contentDescription = stringResource(localizationR.string.pointandshoot_accessibility_shutter),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Button(
                onClick = { changeZoom() },
                modifier = Modifier
                    .width(96.dp)
                    .constrainAs(zoom) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(shutter.end)
                        end.linkTo(parent.end)
                    }
            ) {
                if (zoomed) {
                    Text(text = stringResource(localizationR.string.pointandshoot_35_mm))
                } else {
                    Text(text = stringResource(localizationR.string.pointandshoot_1x))
                }
            }
        }
    }
}
