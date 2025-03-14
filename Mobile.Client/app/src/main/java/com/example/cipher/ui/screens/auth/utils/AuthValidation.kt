package com.example.cipher.ui.screens.auth.utils

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
    ){
        override fun validate(input: String): Boolean {
            return true
        }
    }

    data object PasswordValidation : AuthValidation(
        regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~]{8,}\$"),
        errorMessage = "Password must be at least 8 characters and include uppercase, lowercase, a number."
    )

    data object EmptyValidation : AuthValidation(
        regex = Regex("^\\S+$"),
        errorMessage = "This field cannot be empty"
    )

    data object UsernameValidation : AuthValidation(
        regex = Regex(".{5,}"),
        errorMessage = "Login must be at least 5 characters long"
    )

    data class CheckIfUserExistsValidation(
        val checkIfUserExists: () -> Boolean
    ) : AuthValidation(
        regex = Regex(""),
        errorMessage = "User with this username is already exist"
    ){
        override fun validate(input: String): Boolean {
            return checkIfUserExists()
        }
        companion object {
            val errorMessage = "User with this username is already exist"
        }
    }

    data object BioValidation : AuthValidation(
        regex = Regex(""),
        errorMessage = ""
    ) {
        override fun validate(input: String): Boolean {
            return true
        }
    }

    data object BirthDateValidation : AuthValidation(
        regex = Regex(""),
        errorMessage = ""
    ) {
        override fun validate(input: String): Boolean {
            return true
        }
    }

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
