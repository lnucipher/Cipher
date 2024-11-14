package com.example.cipher.ui.screens.auth.register.models

data class SignUpState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",
    val bio: String = "",
    val birthDate: String = "",
    val avatarUrl: String = "",
)

data class SignUpValidationState(
    val isUsernameValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isConfirmPasswordValid: Boolean = true,
    val isNameValid: Boolean = true,
    val isBioValid: Boolean = true,
    val isBirthDateValid: Boolean = true,

    val usernameErrorMessage: String = ""
)