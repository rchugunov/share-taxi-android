package com.github.rchugunov.weather

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val service = RetrofitClient.forecastsService
    private val datasource = ForecastsWebDatasource(service)
    private val localPrefs = LocalPrefs.Impl(context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE))
    private val repository = ForecastRepository(datasource, localPrefs)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ForecastsViewModel::class.java -> ForecastsViewModel(repository) as T
            else -> throw IllegalStateException("Unknown viewmodel class")
        }
    }

    companion object {
        private const val PREFS_KEY = "PREFS_KEY"
    }
}