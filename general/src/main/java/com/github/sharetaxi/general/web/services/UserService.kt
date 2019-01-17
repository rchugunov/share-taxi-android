package com.github.sharetaxi.general.web.services

import com.github.sharetaxi.general.web.response.UserResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserService {
    @GET("user/{id}")
    fun userProfile(
        @Header("token") token: String,
        @Path("id") userId: String
    ): Deferred<Response<UserResponse>>
}