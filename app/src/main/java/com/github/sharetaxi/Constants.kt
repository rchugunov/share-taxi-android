package com.github.sharetaxi

object Constants {
    const val SUCCESS_RESULT = 0
    const val FAILURE_RESULT = 1
    const val PACKAGE_NAME = "com.github.sharetaxi"
    const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
    const val RESULT_DATA_KEY = "$PACKAGE_NAME.RESULT_DATA_KEY"
    const val LOCATION_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"
    const val REQUEST_CODE_NEW_LOCATION = 1
    const val BASE_URL = "${BuildConfig.BASE_URL}/${BuildConfig.API_VERSION}/"
    const val TEST_URL = "https://rchugunov-share-taxi-pr-2.herokuapp.com/api/${BuildConfig.API_VERSION}/"
    const val TEST_URL2 = "http://192.168.0.103:8080/api/${BuildConfig.API_VERSION}/"
}