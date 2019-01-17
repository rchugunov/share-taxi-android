package com.github.sharetaxi.usecase

import android.content.SharedPreferences
import android.util.Log
import com.github.sharetaxi.general.web.RetrofitClient
import com.github.sharetaxi.general.web.request.FacebookLoginRequest
import java.util.*

class SearchCompanionUsecase(
    private val authPrefs: SharedPreferences,
    private val client: RetrofitClient
) {

    fun checkAuth() = authPrefs.getString(AUTH_TOKEN, null) != null

    suspend fun login(userId: String, token: String, expires: Date): Boolean {
        Log.d(TAG, "User logged in $userId $token $expires")
        val rawResponse = client.authService.facebookLogin(
            FacebookLoginRequest(
                token,
                userId
            )
        ).await()
        val body = rawResponse.body()

        body?.token?.apply {
            authPrefs.edit().putString(AUTH_TOKEN, token).apply()
        }

        body?.user?.apply {
            authPrefs.edit().putString(USER_EMAIL, email).apply()
        }

        return body?.token != null
    }

    fun getUser() = authPrefs.getString(USER_EMAIL, null)

    fun logout() {
        authPrefs.edit().clear().apply()
    }

    companion object {
        private const val AUTH_TOKEN = "AUTH_TOKEN"
        private const val USER_EMAIL = "USER_EMAIL"
        private const val TAG = "AuthRepository"
    }
}
