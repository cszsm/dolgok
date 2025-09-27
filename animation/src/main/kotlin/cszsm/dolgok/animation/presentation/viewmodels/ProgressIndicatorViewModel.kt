package cszsm.dolgok.animation.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cszsm.dolgok.animation.domain.models.StepStatus
import cszsm.dolgok.animation.domain.usecases.GetComplexDataPart1UseCase
import cszsm.dolgok.animation.domain.usecases.GetComplexDataPart2UseCase
import cszsm.dolgok.animation.domain.usecases.GetSimpleDataUseCase
import cszsm.dolgok.animation.presentation.states.ProgressIndicatorScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ProgressIndicatorViewModel(
    val getSimpleDataUseCase: GetSimpleDataUseCase,
    val getComplexDataPart1UseCase: GetComplexDataPart1UseCase,
    val getComplexDataPart2UseCase: GetComplexDataPart2UseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow(ProgressIndicatorScreenState(stepStatus = INITIAL_STEP_STATUS))
    val state: StateFlow<ProgressIndicatorScreenState> = _state.asStateFlow()

    private var loadingJob: Job? = null

    fun load() {
        Log.i(TAG, "loading started")

        if (loadingJob?.isActive == true) {
            cancel()
        }

        loadingJob = viewModelScope.launch {

            increaseProgressByOneStep()

            launch {
                val simpleData = getSimpleDataUseCase()
                increaseProgressByOneStep()
                Log.i(TAG, "simpleData loaded: $simpleData")
            }

            val complexDataPart1Job = async {
                val complexDataPart1 = getComplexDataPart1UseCase()
                increaseProgressByOneStep()
                Log.i(TAG, "complexDataPart1 loaded: $complexDataPart1")
                return@async complexDataPart1
            }

            val complexDataPart1 = complexDataPart1Job.await()
            val complexDataPart2Job = launch {
                val complexDataPart2 = getComplexDataPart2UseCase(part1 = complexDataPart1)
                increaseProgressByOneStep()
                Log.i(TAG, "complexDataPart2 loaded: $complexDataPart2")
            }

            complexDataPart2Job.join()
            Log.i(TAG, "loading finished")
        }
    }

    fun cancel() {
        loadingJob?.cancel()
        Log.i(TAG, "loading cancelled")
        reset()
    }

    fun reset() {
        _state.update { state -> state.copy(stepStatus = INITIAL_STEP_STATUS) }
        Log.i(TAG, "reset")
    }

    private fun increaseProgressByOneStep() {
        _state.update { state ->
            state.copy(
                stepStatus = state.stepStatus.copy(
                    loadedStepCount = state.stepStatus.loadedStepCount + 1
                ),
            )
        }
    }

    private companion object {
        val TAG = ProgressIndicatorViewModel::class.simpleName

        val INITIAL_STEP_STATUS = StepStatus(
            allStepCount = 4,
            loadedStepCount = 0,
        )
    }
}