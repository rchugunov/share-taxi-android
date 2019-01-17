package com.github.sharetaxi.general.exception

class NotAuthorizedException(override val message: String? = null, override val cause: Throwable? = null) : Exception(message, cause)