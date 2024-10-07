package com.example.cipher.ui.screens.auth_screen.register_screen.models

sealed class SignUpUiEvent {
    data class UsernameChanged(val value: String) : SignUpUiEvent()
    data class PasswordChanged(val value: String) : SignUpUiEvent()
    data class ConfirmPasswordChanged(val value: String) : SignUpUiEvent()
    data class NamedChanged(val value: String) : SignUpUiEvent()
    data class BioChanged(val value: String) : SignUpUiEvent()
    data class BirthDateChanged(val value: String) : SignUpUiEvent()
    data class AvatarUrlChanged(val value: String) : SignUpUiEvent()
    data object SignUp : SignUpUiEvent()
}
