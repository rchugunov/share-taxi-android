package com.github.sharetaxi

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface ForecastsService {
    @GET("{api_key}/{lat},{long}")
    fun getForecast(
        @Path("api_key") apiKey: String,
        @Path("lat") lat: Double,
        @Path("long") long: Double
    ): Deferred<Forecast>
}