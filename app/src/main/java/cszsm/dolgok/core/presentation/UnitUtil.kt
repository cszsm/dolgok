package cszsm.dolgok.core.presentation


fun Float.asTemperature() = "$this ${Typography.degree}C"

fun Float.asRain() = "$this mm"

fun Float.asPressure() = "$this hPa"