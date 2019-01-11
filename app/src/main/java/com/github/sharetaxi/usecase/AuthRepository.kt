package com.github.sharetaxi.usecase

import android.content.SharedPreferences
import android.util.Log
import com.github.sharetaxi.FacebookLoginRequest
import com.github.sharetaxi.RetrofitClient
import java.util.*

class AuthRepository(
    private val authPrefs: SharedPreferences,
    private val client: RetrofitClient
) {

    fun checkAuth() = authPrefs.getString(AUTH_TOKEN, null) != null

    suspend fun login(userId: String, token: String, expires: Date): Boolean {
        Log.d(TAG, "User logged in $userId $token $expires")
        val rawResponse = client.authService.facebookLogin(FacebookLoginRequest(token, userId)).await()
        val body = rawResponse.body()
        return body?.loggedIn ?: false
    }

    companion object {
        private const val AUTH_TOKEN = "AUTH_TOKEN"
        private const val TAG = "AuthRepository"
    }

}
