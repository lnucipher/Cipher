package com.example.cipher.ui.screens.auth.models

import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest

sealed class AuthUiEvent {
    data class SignUp(val value: SignUpRequest, val avatarUrl: String?) : AuthUiEvent()
    data class SignIn(val value: SignInRequest) : AuthUiEvent()
}