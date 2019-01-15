package com.github.sharetaxi.profile.vm

import com.mvi.view.MVIView
import com.mvi.vm.MviBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel

class ProfileViewModel : MviBaseViewModel<ProfileViewState, ProfileStateChanges>(
    uiDispatcher = Dispatchers.Main,
    bgDispatcher = Dispatchers.IO
) {
    override val initialViewState = ProfileViewState()

    override suspend fun bindIntentsActual(): Array<ReceiveChannel<ProfileStateChanges>> {
        return emptyArray()
    }

    override fun handleStateChanges(
        previousState: ProfileViewState,
        stateChanges: ProfileStateChanges
    ): ProfileViewState {
        return previousState
    }
}

data class ProfileViewState(val email: String? = null) : MVIView.ViewState

class ProfileStateChanges