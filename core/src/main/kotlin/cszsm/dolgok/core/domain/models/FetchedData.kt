package cszsm.dolgok.core.domain.models

import cszsm.dolgok.core.domain.error.DataError

/**
 * Data that is fetched, therefore can be in different states.
 * If used to represent data that is fetched in multiple batches,
 * it can hold `data` while `loading` is true or `error` is non-null,
 * if the last call is loading or failed respectively.
 *
 * TODO: maybe find a better name
 */
data class FetchedData<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val error: DataError? = null,
)