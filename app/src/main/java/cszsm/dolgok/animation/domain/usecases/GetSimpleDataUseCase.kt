package cszsm.dolgok.animation.domain.usecases

import kotlinx.coroutines.delay

class GetSimpleDataUseCase {

    suspend operator fun invoke(): String {
        delay(LOADING_TIME_MILLIS)
        return DUMMY_RESPONSE
    }

    private companion object {
        const val LOADING_TIME_MILLIS = 3000L
        const val DUMMY_RESPONSE = "simple"
    }
}