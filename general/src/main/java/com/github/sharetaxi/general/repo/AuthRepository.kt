package com.github.sharetaxi.general.repo

import android.util.Log
import com.github.sharetaxi.general.local.AuthLocalDataSource
import com.github.sharetaxi.general.web.request.FacebookLoginRequest
import com.github.sharetaxi.general.web.response.AuthResponse
import com.github.sharetaxi.general.web.services.AuthService
import retrofit2.Response
import java.util.*

class AuthRepository(
    private val authLocal: AuthLocalDataSource,
    private val authService: AuthService
) {

    fun checkAuth() = authLocal.getToken() != null

    suspend fun login(userId: String, token: String, expires: Date): Boolean {
        Log.d(TAG, "User logged in $userId $token $expires")
        val rawResponse: Response<AuthResponse>
        try {
            rawResponse = authService.facebookLogin(
                FacebookLoginRequest(token, userId)
            ).await()
        } catch (e: Exception) {
            return false
        }
        val body = rawResponse.body()

        body?.token?.apply { authLocal.saveToken(this) }

        body?.user?.apply { authLocal.saveUserId(this.id) }

        return body?.token != null
    }

    fun logout() = authLocal.logout()

    companion object {
        private const val TAG = "AuthRepository"
    }
}
