package cszsm.dolgok.animation.presentation.states

import cszsm.dolgok.animation.domain.models.StepStatus

internal data class ProgressIndicatorScreenState(
    val stepStatus: StepStatus,
)