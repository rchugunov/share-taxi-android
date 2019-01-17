package com.github.sharetaxi.general.web.request

data class FacebookLoginRequest(
    val token: String,
    val user_id: String
)
