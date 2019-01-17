package com.github.sharetaxi

import androidx.multidex.MultiDexApplication
import com.github.sharetaxi.general.GENERAL_KOIN_MODULES
import com.github.sharetaxi.koin.usecaseModule
import com.github.sharetaxi.koin.viewModelModule
import com.github.sharetaxi.profile.PROFILE_KOIN_MODULES
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.startKoin

@ExperimentalCoroutinesApi
class ShareTaxiApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        startKoin(
            androidContext = this,
            modules = listOf(
                viewModelModule,
                usecaseModule
            ).plus(PROFILE_KOIN_MODULES).plus(GENERAL_KOIN_MODULES)
        )
    }
}
