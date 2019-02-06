package com.github.sharetaxi.koin

import com.github.sharetaxi.BuildConfig
import com.github.sharetaxi.LoginViewModel
import com.github.sharetaxi.general.Constants
import com.github.sharetaxi.general.repo.SearchRepository
import com.github.sharetaxi.map.vm.MapViewModel
import com.github.sharetaxi.usecase.CheckAuthUsecase
import com.github.sharetaxi.usecase.LoginUsecase
import com.github.sharetaxi.usecase.LoginViaFacebookUsecase
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { LoginViewModel(get<CheckAuthUsecase>(), get<LoginViaFacebookUsecase>(), get<LoginUsecase>()) }
    viewModel { MapViewModel(get<SearchRepository>()) }
}

val usecaseModule = module {
    single { LoginViaFacebookUsecase(get()) }
    single { CheckAuthUsecase(get()) }
    single { LoginUsecase(get()) }
}

val appModule = module {
    single<String>("BASE_URL") { if (BuildConfig.DEBUG) Constants.TEST_URL else Constants.BASE_URL }
}