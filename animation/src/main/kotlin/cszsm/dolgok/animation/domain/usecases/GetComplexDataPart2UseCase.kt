package cszsm.dolgok.animation.domain.usecases

import kotlinx.coroutines.delay

internal class GetComplexDataPart2UseCase {

    suspend operator fun invoke(
        part1: String,
    ): String {
        delay(LOADING_TIME_MILLIS)
        return "${part1}$DUMMY_RESPONSE_PART_2"
    }

    private companion object {
        const val LOADING_TIME_MILLIS = 3000L
        const val DUMMY_RESPONSE_PART_2 = "lex"
    }
}