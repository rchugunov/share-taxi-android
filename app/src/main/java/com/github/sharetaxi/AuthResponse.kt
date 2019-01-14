package com.github.sharetaxi

class AuthResponse(
    val token: String? = null,
    val user: User? = null,
    val message: String = "",
    val exception: String = ""
)
