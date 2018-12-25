package com.github.rchugunov.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class ForecastsViewModel : ViewModel() {

    val forecastsHistoryLiveData: LiveData<List<Forecast>> = MutableLiveData()
    private val forecastRepository = ForecastRepository()
    private val localPrefs : LocalPrefs = LocalPrefs.Impl()

    fun addNewLocation(location: LatLng?) {

    }

    fun loadHistoryResults() {
//        forecastRepository.loc
    }
}