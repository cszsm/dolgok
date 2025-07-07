package cszsm.dolgok.core.domain.result

import cszsm.dolgok.core.domain.error.Error

// TODO: rename to avoid import issues
sealed interface Result<out D, out E : Error> {
    data class Success<out D, out E : Error>(val data: D) : Result<D, E>
    data class Failure<out D, out E : Error>(val error: E) : Result<D, E>

    val successful: Boolean
        get() = this is Success<*, *>
}