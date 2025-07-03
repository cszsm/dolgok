package cszsm.dolgok.core.domain.result

import cszsm.dolgok.core.domain.error.Error

// TODO: rename to avoid import issues
sealed interface Result<out D, out E : Error> {
    data class Success<out D, out E : Error>(val data: D) : Result<D, E>
    data class Failure<out D, out E : Error>(val error: E) : Result<D, E>

    companion object {
        fun <D> success(data: D) = Success<D, Error>(data = data)
        fun <E : Error> failure(error: E) = Failure<Any, E>(error = error)
    }
}