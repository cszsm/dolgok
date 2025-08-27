package cszsm.dolgok.core.domain.model

import cszsm.dolgok.core.domain.error.DataError

sealed interface LoadedData<out D> {
    val data: D?

    data class Loading<out D>(override val data: D? = null) : LoadedData<D>
    data class Success<out D>(override val data: D) : LoadedData<D>
    data class Failure<out D>(override val data: D? = null, val error: DataError) : LoadedData<D>
}