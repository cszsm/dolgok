package cszsm.dolgok.core.domain.error

enum class DataError : Error {
    INCOMPLETE_DATA,
    WRONG_DATA_FORMAT,
    NETWORK,
    UNKNOWN,
}