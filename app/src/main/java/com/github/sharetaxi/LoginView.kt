package com.github.sharetaxi

import com.mvi.view.MVIView

interface LoginView : MVIView<LoginViewState>

data class LoginViewState(
    val loggedIn: Boolean = false,
    val error: Exception? = null
) : MVIView.ViewState