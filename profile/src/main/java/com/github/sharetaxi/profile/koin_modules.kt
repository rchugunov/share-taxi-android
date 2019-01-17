package com.github.sharetaxi.profile

import com.github.sharetaxi.profile.usecase.GetUserProfileUsecase
import com.github.sharetaxi.profile.vm.ProfileViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

@ExperimentalCoroutinesApi
private val vmKoinModule = module {
    viewModel { ProfileViewModel(get()) }
}

@ExperimentalCoroutinesApi
private val usecaseKoinModule = module {
    single { GetUserProfileUsecase(get()) }
}

@ExperimentalCoroutinesApi
val PROFILE_KOIN_MODULES = arrayOf(vmKoinModule, usecaseKoinModule)