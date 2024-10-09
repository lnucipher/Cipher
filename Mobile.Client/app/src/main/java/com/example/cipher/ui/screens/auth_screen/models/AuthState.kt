package com.example.cipher.ui.screens.auth_screen.models

import com.example.cipher.ui.screens.auth_screen.login_screen.models.LoginState
import com.example.cipher.ui.screens.auth_screen.register_screen.models.SignUpState

data class AuthState (
    val isLoading: Boolean = false,
    val login: LoginState = LoginState(),
    val signUp: SignUpState = SignUpState()
)
