package com.github.sharetaxi

import io.reactivex.Single

class ForecastRepository(
    private val forecastsWebDatasource: ForecastsWebDatasource,
    private val localPrefs: LocalPrefs
) {
    fun getForecasts(): Single<List<Forecast>> {
        return localPrefs.getCoordinates()
            .flatMapIterable { it }
            .flatMap { forecastsWebDatasource.getForecast(it.lat, it.long) }
            .toList()
    }

    fun addNewForecast(coordinates: Coordinates) = localPrefs.addCoordinates(coordinates)
        .flatMapIterable { it }
        .flatMap { forecastsWebDatasource.getForecast(it.lat, it.long) }
        .toList()
}