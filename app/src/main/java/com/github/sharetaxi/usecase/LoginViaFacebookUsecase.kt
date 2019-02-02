package com.github.sharetaxi.usecase

import com.facebook.login.LoginManager
import com.github.sharetaxi.general.repo.AuthRepository
import java.util.*

class LoginViaFacebookUsecase(private val authRepository: AuthRepository) {
    suspend fun tryLogin(userId: String, token: String, expires: Date): Pair<Boolean, Exception?> {
        return authRepository.login(userId, token, expires).also { result ->
            if (!result.first){
                LoginManager.getInstance().logOut()
            }
        }
    }
}
