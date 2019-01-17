package com.github.sharetaxi.general.web.services

import com.github.sharetaxi.general.web.request.FacebookLoginRequest
import com.github.sharetaxi.general.web.response.AuthResponse
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