package com.example.cipher.ui.screens.auth_screen.models

data class AuthState (
    val isLoading: Boolean = false,
    val login: LoginState = LoginState(),
    val signUp: SignUpState = SignUpState()
)

data class LoginState(
    val username: String = "",
    val password: String = "",
)

data class SignUpState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",
    val bio: String = "",
    val birthDate: String = "",
    val avatarUrl: String = "",
)
