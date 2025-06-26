package cszsm.dolgok.forecast.domain.models

enum class ForecastDay(
    val offset: Int,
) {
    TODAY(offset = 0),
    TOMORROW(offset = 1),
    THE_DAY_AFTER_TOMORROW(offset = 2),
}