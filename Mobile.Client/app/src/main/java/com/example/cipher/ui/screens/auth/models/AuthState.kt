package com.example.cipher.ui.screens.auth.models

import com.example.cipher.ui.screens.auth.login.models.LoginState
import com.example.cipher.ui.screens.auth.register.models.SignUpState

data class AuthState (
    val isLoading: Boolean = false,
    var showErrorDialog: Boolean = false,
    val login: LoginState = LoginState(),
    val signUp: SignUpState = SignUpState()
)
