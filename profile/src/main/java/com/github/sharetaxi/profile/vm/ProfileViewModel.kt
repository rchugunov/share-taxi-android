package com.github.sharetaxi.profile.vm

import com.github.sharetaxi.general.model.UserProfile
import com.github.sharetaxi.profile.usecase.GetUserProfileStateChanges
import com.github.sharetaxi.profile.usecase.GetUserProfileUsecase
import com.github.sharetaxi.profile.usecase.LogoutStateChanges
import com.github.sharetaxi.profile.usecase.LogoutUsecase
import com.mvi.view.MVIView
import com.mvi.vm.MviBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.flatMap

@ExperimentalCoroutinesApi
internal class ProfileViewModel(
    private val getUserProfileUsecase: GetUserProfileUsecase,
    private val logoutUsecase: LogoutUsecase
) : MviBaseViewModel<ProfileViewState, ProfileStateChanges>(
    uiDispatcher = Dispatchers.Main,
    bgDispatcher = Dispatchers.IO
) {
    override val initialViewState = ProfileViewState()
    private val loadProfileIntent = ConflatedBroadcastChannel<Unit>()
    private val logoutIntent = ConflatedBroadcastChannel<Unit>()

    override suspend fun bindIntentsActual(): Array<ReceiveChannel<ProfileStateChanges>> {
        return arrayOf(
            loadProfileIntent.openSubscription().flatMap { getUserProfileUsecase.loadProfile() },
            logoutIntent.openSubscription().flatMap { logoutUsecase.logout() }
        )
    }

    override fun clearSingleEventsState(vs: ProfileViewState): ProfileViewState {
        return vs.copy(
            userProfileException = null,
            isUserProfileLoading = false,
            loggedOut = false
        )
    }

    override fun handleStateChanges(
        previousState: ProfileViewState,
        stateChanges: ProfileStateChanges
    ): ProfileViewState {
        return when (stateChanges) {
            is GetUserProfileStateChanges.Started, LogoutStateChanges.Started ->
                previousState.copy(isUserProfileLoading = true)
            is GetUserProfileStateChanges.Loaded ->
                previousState.copy(user = stateChanges.userProfile)
            is GetUserProfileStateChanges.Error ->
                previousState.copy(user = null, userProfileException = stateChanges.exception)
            is LogoutStateChanges.Success ->
                previousState.copy(loggedOut = true)
            is LogoutStateChanges.Error ->
                previousState.copy(user = null, userProfileException = stateChanges.exception)
            else -> throw Exception("Unknown stateChanges received $stateChanges")
        }
    }

    fun loadProfile() = loadProfileIntent.offer(Unit)
    fun logout() = logoutIntent.offer(Unit)
}

internal data class ProfileViewState(
    val user: UserProfile? = null,
    val isUserProfileLoading: Boolean = false,
    val userProfileException: Exception? = null,
    val loggedOut: Boolean = false
) : MVIView.ViewState

internal interface ProfileStateChanges
