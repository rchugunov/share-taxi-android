package com.github.sharetaxi.profile.vm

import com.github.sharetaxi.general.model.UserProfile
import com.github.sharetaxi.profile.usecase.GetUserProfileStateChanges
import com.github.sharetaxi.profile.usecase.GetUserProfileUsecase
import com.mvi.view.MVIView
import com.mvi.vm.MviBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.flatMap

@ExperimentalCoroutinesApi
internal class ProfileViewModel(private val getUserProfileUsecase: GetUserProfileUsecase) :
    MviBaseViewModel<ProfileViewState, ProfileStateChanges>(
    uiDispatcher = Dispatchers.Main,
    bgDispatcher = Dispatchers.IO
) {
    override val initialViewState = ProfileViewState()
    private val loadProfileIntent = ConflatedBroadcastChannel<Unit>()

    override suspend fun bindIntentsActual(): Array<ReceiveChannel<ProfileStateChanges>> {
        return arrayOf(loadProfileIntent.openSubscription()
            .flatMap { getUserProfileUsecase.loadProfile() })
    }

    override fun handleStateChanges(
        previousState: ProfileViewState,
        stateChanges: ProfileStateChanges
    ): ProfileViewState {
        return when (stateChanges) {
            is GetUserProfileStateChanges.Started -> previousState.copy(
                isUserProfileLoading = true,
                userProfileException = null
            )
            is GetUserProfileStateChanges.Loaded -> previousState.copy(
                user = stateChanges.userProfile,
                userProfileException = null,
                isUserProfileLoading = false
            )
            is GetUserProfileStateChanges.Error -> previousState.copy(
                user = null,
                isUserProfileLoading = false,
                userProfileException = stateChanges.exception
            )
            else -> throw Exception("Unknown stateChanges received $stateChanges")
        }
    }

    fun loadProfile() = loadProfileIntent.offer(Unit)
    fun logout() {

    }
}

internal data class ProfileViewState(
    val user: UserProfile? = null,
    val isUserProfileLoading: Boolean = false,
    val userProfileException: Exception? = null
) : MVIView.ViewState

internal interface ProfileStateChanges
