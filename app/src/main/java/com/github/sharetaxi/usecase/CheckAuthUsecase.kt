package com.github.sharetaxi.usecase

class CheckAuthUsecase(private val authRepository: AuthRepository) {
    fun checkAuth() = authRepository.checkAuth()

}
