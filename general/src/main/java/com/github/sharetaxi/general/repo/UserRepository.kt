package com.github.sharetaxi.general.repo

import com.github.sharetaxi.general.exception.NotAuthorizedException
import com.github.sharetaxi.general.local.AuthLocalDataSource
import com.github.sharetaxi.general.model.UserProfile
import com.github.sharetaxi.general.web.services.UserService

class UserRepository(private val userService: UserService, private val authLocal: AuthLocalDataSource) :
    BaseRepository() {
    suspend fun loadUser(): UserProfile {
        val token = authLocal.getToken() ?: throw NotAuthorizedException("token is null")
        val userId = authLocal.getUserId() ?: throw NotAuthorizedException("userId is null")
        val deferredResponse = userService.userProfile(token = token, userId = userId)
        val response = deferredResponse.await()
        if (response.isSuccessful) {
            return response.body()?.userProfile!!
        } else {
            throw handleBrokenResponse(response = response)
        }
    }
}