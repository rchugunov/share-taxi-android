package com.github.sharetaxi

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login/fb")
    fun facebookLogin(
        @Body request: FacebookLoginRequest
    ): Deferred<Response<AuthResponse>>
}