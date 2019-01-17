package com.github.sharetaxi.usecase

import com.github.sharetaxi.general.repo.AuthRepository

class CheckAuthUsecase(private val authRepository: AuthRepository) {
    fun checkAuth() = authRepository.checkAuth()
}
