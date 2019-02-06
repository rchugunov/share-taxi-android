package com.github.sharetaxi.general.ext

import com.crashlytics.android.Crashlytics

fun Throwable.toCrashlytics() {
    Crashlytics.logException(this)
}