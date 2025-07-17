package cszsm.dolgok.animation.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cszsm.dolgok.R
import cszsm.dolgok.animation.presentation.viewmodels.ProgressIndicatorViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProgressIndicatorScreen(
    viewModel: ProgressIndicatorViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProgressIndicatorContent(
        loading = state.stepStatus.loading,
        loaded = state.stepStatus.loaded,
        progress = state.stepStatus.progress,
        onLoad = viewModel::load,
        onCancel = viewModel::cancel,
        onReset = viewModel::reset,
    )
}

@Composable
private fun ProgressIndicatorContent(
    loading: Boolean,
    loaded: Boolean,
    progress: Float,
    onLoad: () -> Unit,
    onCancel: () -> Unit,
    onReset: () -> Unit,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    )

    Scaffold(
        bottomBar = {
            BottomBar(
                loading = loading,
                loaded = loaded,
                onLoad = onLoad,
                onCancel = onCancel,
                onReset = onReset,
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            AnimatedVisibility(
                visible = loading,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier.align(Alignment.Center),
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                )
            }

            AnimatedVisibility(
                visible = loaded,
                enter = scaleIn() + slideIn(initialOffset = { IntOffset(0, 100) }),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier.align(Alignment.Center),
            ) {
                Text(stringResource(R.string.animation_progress_ready))
            }
        }
    }
}

@Composable
private fun BottomBar(
    loading: Boolean,
    loaded: Boolean,
    onLoad: () -> Unit,
    onCancel: () -> Unit,
    onReset: () -> Unit,
) {
    BottomAppBar {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                loaded ->
                    Button(
                        onClick = onReset,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(text = stringResource(R.string.core_command_reset))
                    }

                loading ->
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(text = stringResource(R.string.core_command_cancel))
                    }

                else ->
                    Button(
                        onClick = onLoad,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(text = stringResource(R.string.core_command_load))
                    }
            }
        }
    }
}