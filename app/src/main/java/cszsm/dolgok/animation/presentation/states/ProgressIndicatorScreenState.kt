package cszsm.dolgok.animation.presentation.states

import cszsm.dolgok.animation.domain.models.StepStatus

data class ProgressIndicatorScreenState(
    val stepStatus: StepStatus,
)