package com.github.rchugunov.weather

class ForecastsWebDatasource(private val forecastsService: ForecastsService) {
    fun getForecast(lat: Double, long: Double) = forecastsService.getForecast(Constants.API_KEY, lat, long)
}
