package com.github.sharetaxi.usecase

import java.util.*

class LoginViaFacebookUsecase(private val authRepository: AuthRepository) {
    suspend fun tryLogin(userId: String, token: String, expires: Date): Boolean {
        return authRepository.login(userId, token, expires)
    }
}
