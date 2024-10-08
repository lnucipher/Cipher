package com.example.cipher.ui.screens.auth_screen.utils

sealed class AuthValidation(
    val regex: Regex,
    val errorMessage: String
) {

    open fun validate(input: String): Boolean {
        return regex.matches(input)
    }

    data object NoneValidation : AuthValidation(
        regex = Regex(""),
        errorMessage = ""
    )

    data object PasswordValidation : AuthValidation(
        regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$"),
        errorMessage = "Password must be at least 8 characters long and contain both letters and numbers."
    )

    data object EmptyValidation : AuthValidation(
        regex = Regex("^\\S+$"),
        errorMessage = "This field cannot be empty"
    )

    data object LoginValidation : AuthValidation(
        regex = Regex(".{5,}"),  // Minimum 5 symbols
        errorMessage = "Login must be at least 5 characters long"
    )

    data object BirthDateValidation : AuthValidation(
        regex = Regex("^\\d{2}\\.\\d{2}\\.\\d{4}\$"),
        errorMessage = "Birthdate must be in format dd.mm.yyyy"
    )

    data class ConfirmPasswordValidation(
        private val originalPassword: String
    ) : AuthValidation(
        regex = Regex("^\\S+$"),
        errorMessage = "Passwords do not match"
    ) {
        override fun validate(input: String): Boolean {
            return input == originalPassword
        }
    }
}
