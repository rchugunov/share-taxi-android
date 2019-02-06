package com.github.sharetaxi

import com.facebook.AccessToken
import com.github.sharetaxi.usecase.CheckAuthUsecase
import com.github.sharetaxi.usecase.LoginUsecase
import com.github.sharetaxi.usecase.LoginViaFacebookUsecase
import com.mvi.vm.MviBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.map

class LoginViewModel(
    private val checkAuthUsecase: CheckAuthUsecase,
    private val loginViaFacebookUsecase: LoginViaFacebookUsecase,
    private val loginUsecase: LoginUsecase
) :
    MviBaseViewModel<LoginViewState, LoginViewModel.StateChanges>(
        bgDispatcher = Dispatchers.IO,
        uiDispatcher = Dispatchers.Main
    ) {

    override val initialViewState = LoginViewState()

    private val checkLoginStatusIntent = BroadcastChannel<Unit>(Channel.CONFLATED)
    private val loginViaFacebookIntent = BroadcastChannel<AccessToken>(Channel.CONFLATED)
    private val loginIntent = BroadcastChannel<Pair<String, String>>(Channel.CONFLATED)

    override suspend fun bindIntentsActual(): Array<ReceiveChannel<StateChanges>> {
        val login = loginIntent.openSubscription()
            .map { loginUsecase.tryLogin(it.first, it.second) }
            .map {
                if (it.second != null) {
                    StateChanges.Error(it.second!!)
                } else {
                    StateChanges.LoginStatusStateChanges(it.first)
                }
            }

        val checkLoginStatus = checkLoginStatusIntent.openSubscription()
            .map {
                checkAuthUsecase.checkAuth()
            }.map {
                StateChanges.LoginStatusStateChanges(it)
            }
        val loginViaFacebook = loginViaFacebookIntent.openSubscription()
            .map { loginViaFacebookUsecase.tryLogin(it.userId, it.token, it.expires) }
            .map {
                if (it.second != null) {
                    StateChanges.Error(it.second!!)
                } else {
                    StateChanges.LoginStatusStateChanges(it.first)
                }
            }

        return arrayOf(checkLoginStatus, loginViaFacebook, login)
    }

    override fun handleStateChanges(previousState: LoginViewState, stateChanges: StateChanges): LoginViewState {
        return when (stateChanges) {
            is StateChanges.LoginStatusStateChanges -> previousState.copy(loggedIn = stateChanges.loggedIn)
            is StateChanges.Error -> previousState.copy(error = stateChanges.exception)
        }
    }

    fun checkLoginStatus() = checkLoginStatusIntent.offer(Unit)
    fun loginViaFacebook(accessToken: AccessToken) = loginViaFacebookIntent.offer(accessToken)
    fun login(login: String, password: String) = loginIntent.offer(Pair(login, password))

    companion object {
        private val TAG = "LoginViewModel"
    }

    sealed class StateChanges {
        class LoginStatusStateChanges(val loggedIn: Boolean) : StateChanges()
        class Error(val exception: Exception) : StateChanges()
    }
}