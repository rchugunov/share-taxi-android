package com.github.sharetaxi.profile.usecase

import com.github.sharetaxi.general.model.UserProfile
import com.github.sharetaxi.general.repo.UserRepository
import com.github.sharetaxi.profile.vm.ProfileStateChanges
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel

@ExperimentalCoroutinesApi
internal class GetUserProfileUsecase(private val userRepository: UserRepository) {
    suspend fun loadProfile(): ReceiveChannel<GetUserProfileStateChanges> {
        val channel = ConflatedBroadcastChannel<GetUserProfileStateChanges>()
        channel.send(GetUserProfileStateChanges.Started)
        try {
            channel.send(GetUserProfileStateChanges.Loaded(userRepository.loadUser()))
        } catch (exc: java.lang.Exception) {
            channel.send(GetUserProfileStateChanges.Error(exc))
        }
        return channel.openSubscription()
    }
}

sealed class GetUserProfileStateChanges : ProfileStateChanges {
    object Started : GetUserProfileStateChanges()
    class Loaded(val userProfile: UserProfile) : GetUserProfileStateChanges()
    class Error(val exception: Exception) : GetUserProfileStateChanges()
}