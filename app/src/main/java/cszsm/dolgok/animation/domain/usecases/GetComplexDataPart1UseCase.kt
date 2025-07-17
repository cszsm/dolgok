package cszsm.dolgok.animation.domain.usecases

import kotlinx.coroutines.delay

class GetComplexDataPart1UseCase {

    suspend operator fun invoke(): String {
        delay(LOADING_TIME_MILLIS)
        return DUMMY_RESPONSE_PART_1
    }

    private companion object {
        const val LOADING_TIME_MILLIS = 2000L
        const val DUMMY_RESPONSE_PART_1 = "comp"
    }
}