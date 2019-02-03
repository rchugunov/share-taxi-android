package com.github.sharetaxi.usecase

import com.facebook.login.LoginManager
import com.github.sharetaxi.general.repo.AuthRepository

class LoginUsecase(private val authRepository: AuthRepository) {
    suspend fun tryLogin(login: String, password: String): Pair<Boolean, Exception?> {
        return authRepository.login(login, password).also { result ->
            if (!result.first){
                LoginManager.getInstance().logOut()
            }
        }
    }
}
