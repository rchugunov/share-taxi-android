package com.github.sharetaxi.koin

import android.content.Context
import com.github.sharetaxi.LoginViewModel
import com.github.sharetaxi.usecase.AuthRepository
import com.github.sharetaxi.usecase.CheckAuthUsecase
import com.github.sharetaxi.usecase.LoginViaFacebookUsecase
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get()) }
}

val usecaseModule = module {
    single { LoginViaFacebookUsecase(get()) }
    single { CheckAuthUsecase(get()) }
}

val repoModule = module {
    single { AuthRepository(androidContext().getSharedPreferences("auth", Context.MODE_PRIVATE)) }
}