package com.example.cipher.ui.screens.auth.login.models

data class LoginState(
    val username: String = "",
    val password: String = "",
)

data class LoginValidationState (
    val isUsernameValid: Boolean = true,
    val isPasswordValid: Boolean = true
)



