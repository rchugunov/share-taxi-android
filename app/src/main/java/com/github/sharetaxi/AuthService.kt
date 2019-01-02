package com.github.sharetaxi

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("login")
    fun facebookLogin(
        @Query("token") token: String,
        @Query("user_id") userId: String,
        @Query("expiration_ts") expires: Long
    ): Deferred<Response<AuthResponse>>
}