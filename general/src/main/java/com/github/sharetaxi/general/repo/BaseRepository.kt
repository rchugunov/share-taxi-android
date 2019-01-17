package com.github.sharetaxi.general.repo

import retrofit2.Response

open class BaseRepository {
    protected fun handleBrokenResponse(response: Response<*>): WebException {
        return WebException("Web request ${response.raw().request().url()} | error code: ${response.code()} | message: ${response.message()} | server text: ${response.errorBody()?.string()}")
    }

    class WebException(override val message: String? = null, override val cause: Throwable? = null) :
        Exception(message, cause)
}