package com.github.sharetaxi.general

import android.content.Context
import com.github.sharetaxi.general.local.AuthLocalDataSource
import com.github.sharetaxi.general.repo.AuthRepository
import com.github.sharetaxi.general.repo.SearchRepository
import com.github.sharetaxi.general.repo.UserRepository
import com.github.sharetaxi.general.web.RetrofitClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module


private val repoModule = module {
    single { AuthRepository(authLocal = get(), authService = get<RetrofitClient>().authService) }

    single {
        UserRepository(userService = get<RetrofitClient>().userService, authLocal = get())
    }

    single {
        SearchRepository()
    }

    single {
        AuthLocalDataSource(
            authPrefs = androidContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        )
    }
}

private val webModule = module {
    single { RetrofitClient(get<String>("BASE_URL")) }
}

val GENERAL_KOIN_MODULES = arrayOf(repoModule, webModule)