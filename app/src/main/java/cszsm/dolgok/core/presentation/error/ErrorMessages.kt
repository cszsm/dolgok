package cszsm.dolgok.core.presentation.error

import cszsm.dolgok.core.domain.error.DataError

val DataError.message: String
    get() = when (this) {
        DataError.INCOMPLETE_DATA -> "Error: incomplete data"
        DataError.WRONG_DATA_FORMAT -> "Error: wrong data format"
    }