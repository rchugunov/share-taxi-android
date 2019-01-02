package com.github.sharetaxi

class ForecastsWebDatasource(private val forecastsService: ForecastsService) {
    fun getForecast(lat: Double, long: Double) = forecastsService.getForecast("", lat, long)
}
