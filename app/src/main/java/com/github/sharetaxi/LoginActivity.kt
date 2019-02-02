package com.github.sharetaxi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject


class LoginActivity : AppCompatActivity() {

    private val callbackManager by lazy { CallbackManager.Factory.create() }
    private val btnFbLogin by lazy { findViewById<LoginButton>(R.id.btn_login_facebook) }
    private val btnLoginWithPassword by lazy { findViewById<View>(R.id.btn_login_with_password) }

    private val etLogin by lazy { findViewById<EditText>(R.id.et_login) }
    private val etPassoword by lazy { findViewById<EditText>(R.id.et_password) }

    private val vm by inject<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        btnLoginWithPassword.setOnClickListener { vm.login(etLogin.text.toString(), etPassoword.text.toString()) }

        btnFbLogin.setReadPermissions("email")
        btnFbLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                vm.loginViaFacebook(loginResult.accessToken)
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })

        vm.viewState.observe(this, Observer { render(it) })
        vm.checkLoginStatus()
    }

    private fun render(viewState: LoginViewState) {
        if (viewState.loggedIn) {
            finish()
            SearchCompanionsActivity.start(this)
        } else {
            btnFbLogin.visibility = View.VISIBLE
        }

        if (viewState.error != null) {
            Snackbar.make(btnFbLogin, viewState.error.message!!, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}