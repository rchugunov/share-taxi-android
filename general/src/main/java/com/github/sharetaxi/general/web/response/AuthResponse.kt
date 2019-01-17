package com.github.sharetaxi.general.web.response

import com.github.sharetaxi.general.model.User

class AuthResponse (
    val token: String? = null,
    val user: User? = null
) : BaseResponse()
