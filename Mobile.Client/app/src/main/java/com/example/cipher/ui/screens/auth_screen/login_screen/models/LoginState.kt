package com.example.cipher.ui.screens.auth_screen.login_screen.models

data class LoginState(
    val username: String = "",
    val password: String = "",
)

data class LoginValidationState (
    var isUsernameValid: Boolean = true,
    var isPasswordValid: Boolean = true
)



