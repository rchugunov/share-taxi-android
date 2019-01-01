package com.github.sharetaxi

import androidx.multidex.MultiDexApplication
import com.github.sharetaxi.koin.repoModule
import com.github.sharetaxi.koin.usecaseModule
import com.github.sharetaxi.koin.viewModelModule
import org.koin.android.ext.android.startKoin

class ShareTaxiApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(
            viewModelModule,
            usecaseModule,
            repoModule
        ))
    }
}
