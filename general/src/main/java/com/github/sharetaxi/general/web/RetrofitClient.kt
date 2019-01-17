package com.github.sharetaxi.general.web

import com.github.sharetaxi.general.Constants
import com.github.sharetaxi.general.web.services.AuthService
import com.github.sharetaxi.general.web.services.UserService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitClient {

    private val retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .baseUrl(Constants.BASE_URL)
            .build()
    }

    val authService by lazy { retrofit.create(AuthService::class.java) }
    val userService by lazy { retrofit.create(UserService::class.java) }
}