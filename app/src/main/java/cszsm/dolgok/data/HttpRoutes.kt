package cszsm.dolgok.data

object HttpRoutes {

    private const val WEATHER_BASE_URL = "https://api.open-meteo.com"
    const val FORECAST =
        "$WEATHER_BASE_URL/v1/forecast?latitude=47.50&longitude=19.04&timezone=auto&hourly=temperature_2m&forecast_days=1"
}