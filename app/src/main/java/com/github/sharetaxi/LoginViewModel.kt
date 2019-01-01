package com.github.sharetaxi

import com.facebook.AccessToken
import com.github.sharetaxi.usecase.CheckAuthUsecase
import com.github.sharetaxi.usecase.LoginViaFacebookUsecase
import com.mvi.vm.MviBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.map

class LoginViewModel(
    private val checkAuthUsecase: CheckAuthUsecase,
    private val loginViaFacebookUsecase: LoginViaFacebookUsecase
) :
    MviBaseViewModel<LoginViewState, LoginViewModel.StateChanges>(
        bgDispatcher = Dispatchers.IO,
        uiDispatcher = Dispatchers.Main
    ) {

    override val initialViewState = LoginViewState()

    private val checkLoginStatusIntent = BroadcastChannel<Unit>(Channel.CONFLATED)
    private val loginViaFacebookIntent = BroadcastChannel<AccessToken>(Channel.CONFLATED)

    override suspend fun bindIntentsActual(): Array<ReceiveChannel<StateChanges>> {
        val checkLoginStatus = checkLoginStatusIntent.openSubscription()
            .map {
                checkAuthUsecase.checkAuth()
            }.map {
                StateChanges.LoginStatusStateChanges(it)
            }
        val loginViaFacebook = loginViaFacebookIntent.openSubscription()
            .map { loginViaFacebookUsecase.tryLogin(it.userId, it.token, it.expires) }
            .map { StateChanges.LoginStatusStateChanges(it) }

        return arrayOf(checkLoginStatus, loginViaFacebook)
    }

    override fun handleStateChanges(previousState: LoginViewState, stateChanges: StateChanges): LoginViewState {
        return when (stateChanges) {
            is StateChanges.LoginStatusStateChanges -> previousState.copy(loggedIn = stateChanges.loggedIn)
        }
    }

    fun checkLoginStatus() = checkLoginStatusIntent.offer(Unit)
    fun loginViaFacebook(accessToken: AccessToken) = loginViaFacebookIntent.offer(accessToken)

    companion object {
        private val TAG = "LoginViewModel"
    }

    sealed class StateChanges {
        class LoginStatusStateChanges(val loggedIn: Boolean) : StateChanges()
    }
}