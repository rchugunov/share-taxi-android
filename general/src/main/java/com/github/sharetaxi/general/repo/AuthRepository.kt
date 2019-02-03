package com.github.sharetaxi.general.repo

import android.util.Log
import com.github.sharetaxi.general.local.AuthLocalDataSource
import com.github.sharetaxi.general.web.request.BasicLoginRequest
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

    suspend fun loginWithFacebook(userId: String, token: String, expires: Date): Pair<Boolean, Exception?> {
        Log.d(TAG, "User logged in $userId $token $expires")
        val rawResponse: Response<AuthResponse>
        try {
            rawResponse = authService.facebookLogin(
                FacebookLoginRequest(token, userId)
            ).await()

            if (!rawResponse.isSuccessful) {
                return Pair(false, Exception(rawResponse.errorBody()?.string()!!))
            }
        } catch (e: Exception) {
            return Pair(false, e)
        }

        val body = rawResponse.body()
        body?.token?.apply { authLocal.saveToken(this) }
        body?.user?.apply { authLocal.saveUserId(this.id) }

        return Pair(body?.token != null, null)
    }

    suspend fun login(login: String, password: String): Pair<Boolean, Exception?> {
        val rawResponse: Response<AuthResponse>
        try {
            rawResponse = authService.basicLogin(
                BasicLoginRequest(login, password)
            ).await()

            if (!rawResponse.isSuccessful) {
                return Pair(false, Exception(rawResponse.errorBody()?.string()!!))
            }

        } catch (e: Exception) {
            return Pair(false, e)
        }

        val body = rawResponse.body()
        body?.token?.apply { authLocal.saveToken(this) }
        body?.user?.apply { authLocal.saveUserId(this.id) }

        return Pair(body?.token != null, null)
    }

    fun logout() = authLocal.logout()

    companion object {
        private const val TAG = "AuthRepository"
    }
}
