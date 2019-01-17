package com.github.sharetaxi.general.local

import android.content.SharedPreferences

class AuthLocalDataSource(private val authPrefs: SharedPreferences) {

    fun saveToken(token: String) = authPrefs.edit().putString(AUTH_TOKEN, token).apply()
    fun saveUserId(userId: String) = authPrefs.edit().putString(USER_ID, userId).apply()
    fun getUserId(): String? = authPrefs.getString(USER_ID, null)
    fun logout() = authPrefs.edit().clear().apply()
    fun getToken(): String? = authPrefs.getString(AUTH_TOKEN, null)

    companion object {
        private const val AUTH_TOKEN = "AUTH_TOKEN"
        private const val USER_ID = "USER_ID"
    }
}