package cszsm.dolgok.domain.enums

enum class ForecastDay(
    val offset: Int,
) {
    TODAY(offset = 0),
    TOMORROW(offset = 1),
    THE_DAY_AFTER_TOMORROW(offset = 2),
}