package com.github.sharetaxi.profile.usecase

import com.github.sharetaxi.general.repo.AuthRepository
import com.github.sharetaxi.profile.vm.ProfileStateChanges
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel

@ExperimentalCoroutinesApi
internal class LogoutUsecase(private val authRepository: AuthRepository) {
    suspend fun logout(): ReceiveChannel<LogoutStateChanges> {
        val channel = ConflatedBroadcastChannel<LogoutStateChanges>()
        channel.send(LogoutStateChanges.Started)
        try {
            authRepository.logout()
            channel.send(LogoutStateChanges.Success)
        } catch (exc: java.lang.Exception) {
            channel.send(LogoutStateChanges.Error(exc))
        }
        return channel.openSubscription()
    }
}

sealed class LogoutStateChanges : ProfileStateChanges {
    object Started : LogoutStateChanges()
    object Success : LogoutStateChanges()
    class Error(val exception: Exception) : LogoutStateChanges()
}