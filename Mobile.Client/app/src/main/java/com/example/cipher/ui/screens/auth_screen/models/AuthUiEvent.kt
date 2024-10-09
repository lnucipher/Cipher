package com.example.cipher.ui.screens.auth_screen.models

import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest

sealed class AuthUiEvent {
    data class SignUp(val value: SignUpRequest) : AuthUiEvent()
    data class SignIn(val value: SignInRequest) : AuthUiEvent()
}