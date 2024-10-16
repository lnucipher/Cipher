package com.example.cipher.ui.screens.auth.login.models

sealed class LoginUiEvent {
    data class UsernameChanged(val value: String) : LoginUiEvent()
    data class PasswordChanged(val value: String) : LoginUiEvent()
    data object SingIn : LoginUiEvent()
}