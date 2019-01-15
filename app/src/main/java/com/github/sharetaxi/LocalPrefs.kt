package com.github.sharetaxi

import android.content.SharedPreferences


interface LocalPrefs {
    suspend fun getCoordinates(): List<Coordinates>
    suspend fun addCoordinates(coordinates: Coordinates): List<Coordinates>
    suspend fun removeCoordinates(coordinates: Coordinates): List<Coordinates>

    class Impl(private val prefs: SharedPreferences) : LocalPrefs {
//        private val gson = Gson()

        override suspend fun getCoordinates(): List<Coordinates> {
            TODO()
//            val jsonStr = prefs.getString(KEY_ALL_COORDS, null) ?: return@fromCallable emptyList<Coordinates>()
//            return@fromCallable gson.fromJson<List<Coordinates>>(
//                jsonStr,
//                object : TypeToken<List<Coordinates>>() {}.type
//            )
        }

        override suspend fun addCoordinates(coordinates: Coordinates): List<Coordinates> {
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

        override suspend fun removeCoordinates(coordinates: Coordinates): List<Coordinates>{
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
