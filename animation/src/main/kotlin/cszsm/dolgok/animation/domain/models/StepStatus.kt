package cszsm.dolgok.animation.domain.models

internal data class StepStatus(
    val allStepCount: Int,
    val loadedStepCount: Int,
) {
    val loading: Boolean
        get() = loadedStepCount in 1..<allStepCount

    val loaded: Boolean
        get() = allStepCount <= loadedStepCount

    val progress: Float
        get() = (loadedStepCount / allStepCount.toFloat())
            .coerceIn(minimumValue = 0f, maximumValue = 1f)
}
