package com.github.rchugunov.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ForecastsViewModel(private val forecastRepository: ForecastRepository) : ViewModel() {

    val forecastsHistoryLiveData: MutableLiveData<List<Forecast>> = MutableLiveData()
    val errorLoadingLiveData: MutableLiveData<ForecastsException> = MutableLiveData()

    private val subscription = CompositeDisposable()

    fun addNewLocation(location: Coordinates) {
        subscription.add(
            forecastRepository.addNewForecast(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { data -> forecastsHistoryLiveData.value = data },
                    { err ->
                        Log.e(TAG, err.localizedMessage, err)
                        errorLoadingLiveData.value =
                                ForecastsException.AddNewLocationException("Was not able to add coordinates", err)
                    })
        )
    }

    fun loadHistoryResults() {
        subscription.add(
            forecastRepository.getForecasts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { data -> forecastsHistoryLiveData.value = data },
                    { err ->
                        Log.e(TAG, err.localizedMessage, err)
                        errorLoadingLiveData.value =
                                ForecastsException.AddNewLocationException("Was not able to add coordinates", err)
                    })
        )
    }

    companion object {
        private val TAG = "ForecastsViewModel"
    }
}

sealed class ForecastsException(message: String?, cause: Throwable?) : Exception(message, cause) {
    class AddNewLocationException(message: String?, cause: Throwable?) : ForecastsException(message, cause)
}