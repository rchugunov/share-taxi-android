package com.github.sharetaxi

import android.content.SharedPreferences
import io.reactivex.Observable


interface LocalPrefs {
    fun getCoordinates(): Observable<List<Coordinates>>
    fun addCoordinates(coordinates: Coordinates): Observable<List<Coordinates>>
    fun removeCoordinates(coordinates: Coordinates): Observable<List<Coordinates>>

    class Impl(private val prefs: SharedPreferences) : LocalPrefs {
//        private val gson = Gson()

        override fun getCoordinates(): Observable<List<Coordinates>> = Observable.fromCallable {
            TODO()
//            val jsonStr = prefs.getString(KEY_ALL_COORDS, null) ?: return@fromCallable emptyList<Coordinates>()
//            return@fromCallable gson.fromJson<List<Coordinates>>(
//                jsonStr,
//                object : TypeToken<List<Coordinates>>() {}.type
//            )
        }

        override fun addCoordinates(coordinates: Coordinates): Observable<List<Coordinates>> = Observable.fromCallable {
            TODO()
//            val jsonStr = prefs.getString(KEY_ALL_COORDS, null)
//            val values = jsonStr.let {
//                gson.fromJson<List<Coordinates>>(
//                    it,
//                    object : TypeToken<List<Coordinates>>() {}.type
//                )
//            } ?: ArrayList()
//            val newValues = values.plus(coordinates)
//            prefs.edit().putString(KEY_ALL_COORDS, gson.toJson(newValues)).apply()
//            newValues
        }

        override fun removeCoordinates(coordinates: Coordinates): Observable<List<Coordinates>> =
            Observable.fromCallable {
                TODO()
//            val jsonStr = prefs.getString(KEY_ALL_COORDS, null) ?: return@fromCallable emptyList<Coordinates>()
//            val values = gson.fromJson<List<Coordinates>>(
//                jsonStr,
//                object : TypeToken<List<Coordinates>>() {}.type
//            )
//            val newValues = values.minus(coordinates)
//            prefs.edit().putString(KEY_ALL_COORDS, gson.toJson(newValues)).apply()
//            newValues
        }

        companion object {
            private const val KEY_ALL_COORDS = "KEY_ALL_COORDS"
        }
    }

}
