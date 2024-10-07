package com.example.cipher.ui.screens.auth_screen.login_screen.models

sealed class LoginUiEvent {
    data class UsernameChanged(val value: String) : LoginUiEvent()
    data class PasswordChanged(val value: String) : LoginUiEvent()
    object SingIn : LoginUiEvent()
}