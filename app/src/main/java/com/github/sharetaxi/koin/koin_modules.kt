package com.github.sharetaxi.koin

import com.github.sharetaxi.LoginViewModel
import com.github.sharetaxi.usecase.CheckAuthUsecase
import com.github.sharetaxi.usecase.LoginViaFacebookUsecase
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { LoginViewModel(get<CheckAuthUsecase>(), get<LoginViaFacebookUsecase>()) }
}

val usecaseModule = module {
    single { LoginViaFacebookUsecase(get()) }
    single { CheckAuthUsecase(get()) }
}