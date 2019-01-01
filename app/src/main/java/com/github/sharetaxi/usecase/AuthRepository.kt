package com.github.sharetaxi.usecase

import android.content.SharedPreferences
import android.util.Log
import java.util.*

class AuthRepository(private val authPrefs: SharedPreferences) {

    fun checkAuth() = authPrefs.getString(AUTH_TOKEN, null) != null
    fun login(userId: String, token: String, expires: Date): Boolean {
        Log.d(TAG, "User logged in $userId $token $expires")
        return true
    }

    companion object {
        private const val AUTH_TOKEN = "AUTH_TOKEN"
        private const val TAG = "AuthRepository"
    }

}
