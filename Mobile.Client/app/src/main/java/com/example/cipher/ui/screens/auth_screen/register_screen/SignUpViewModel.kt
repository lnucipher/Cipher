package com.example.cipher.ui.screens.auth_screen.register_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cipher.domain.models.auth.SignUpRequest
import com.example.cipher.domain.repository.auth.AuthRepository
import com.example.cipher.ui.screens.auth_screen.AuthViewModel
import com.example.cipher.ui.screens.auth_screen.utils.AuthValidation
import com.example.cipher.ui.screens.auth_screen.models.AuthUiEvent
import com.example.cipher.ui.screens.auth_screen.register_screen.models.SignUpUiEvent
import com.example.cipher.ui.screens.auth_screen.register_screen.models.SignUpValidationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private lateinit var authViewModel: AuthViewModel

    var validationState by mutableStateOf(SignUpValidationState())

    fun setAuthViewModel(authViewModel: AuthViewModel) {
        this.authViewModel = authViewModel
    }

    fun validateSignUpFields(): Boolean {
        with(authViewModel.state.signUp) {
            val isUsernameValid = AuthValidation.UsernameValidation.validate(username)
            val usernameExists = if (isUsernameValid) AuthValidation
                .CheckIfUserExistsValidation(checkIfUserExists = {
                    runBlocking {
                        repository.checkIdUserExist(username)
                    }
                }).validate(username)
            else false

            validationState = validationState.copy(
                isUsernameValid = isUsernameValid && usernameExists,
                isPasswordValid = AuthValidation.PasswordValidation.validate(password),
                isConfirmPasswordValid = AuthValidation.ConfirmPasswordValidation(password).validate(confirmPassword),
                usernameErrorMessage = when {
                    !isUsernameValid -> AuthValidation.UsernameValidation.errorMessage
                    !usernameExists -> AuthValidation.CheckIfUserExistsValidation.errorMessage
                    else -> ""
                }
            )
        }
        return validationState.run {
            isUsernameValid &&
            isPasswordValid &&
            isConfirmPasswordValid
        }
    }

    private fun validateFields(): Boolean {
        with(authViewModel.state.signUp) {
            validationState = validationState.copy(
                isUsernameValid = AuthValidation.UsernameValidation.validate(username),
                isPasswordValid = AuthValidation.PasswordValidation.validate(password),
                isConfirmPasswordValid = AuthValidation.ConfirmPasswordValidation(password).validate(confirmPassword),
                isNameValid = AuthValidation.EmptyValidation.validate(name),
                isBioValid = AuthValidation.BioValidation.validate(bio),
                isBirthDateValid = AuthValidation.BirthDateValidation.validate(birthDate)
            )
        }
        return validationState.run {
            isUsernameValid &&
            isPasswordValid &&
            isConfirmPasswordValid &&
            isNameValid &&
            isBioValid &&
            isBirthDateValid
        }
    }

    fun onEvent(event: SignUpUiEvent) {
        val currentState = authViewModel.state

        when (event) {
            is SignUpUiEvent.UsernameChanged -> {
                authViewModel.state = currentState.copy(
                    signUp = currentState.signUp.copy(username = event.value)
                )
            }
            is SignUpUiEvent.PasswordChanged -> {
                authViewModel.state = currentState.copy(
                    signUp = currentState.signUp.copy(password = event.value)
                )
            }
            is SignUpUiEvent.ConfirmPasswordChanged -> {
                authViewModel.state = currentState.copy(
                    signUp = currentState.signUp.copy(confirmPassword = event.value)
                )
            }
            is SignUpUiEvent.NamedChanged -> {
                authViewModel.state = currentState.copy(
                    signUp = currentState.signUp.copy(name = event.value)
                )
            }
            is SignUpUiEvent.BioChanged -> {
                authViewModel.state = currentState.copy(
                    signUp = currentState.signUp.copy(bio = event.value)
                )
            }
            is SignUpUiEvent.BirthDateChanged -> {
                authViewModel.state = currentState.copy(
                    signUp = currentState.signUp.copy(birthDate = event.value)
                )
            }
            is SignUpUiEvent.AvatarUrlChanged -> {
                authViewModel.state = currentState.copy(
                    signUp = currentState.signUp.copy(avatarUrl = event.value)
                )
            }
            is SignUpUiEvent.SignUp -> {
                if (validateFields()) {
                    authViewModel.onEvent(
                        AuthUiEvent.SignUp(
                            SignUpRequest(
                                username = currentState.signUp.username,
                                password = currentState.signUp.password,
                                name = currentState.signUp.name,
                                bio = currentState.signUp.bio.takeIf { it.isNotEmpty() },
                                birthDate = currentState.signUp.birthDate.takeIf { it.isNotEmpty() }
                            ),
                            avatarUrl = currentState.signUp.avatarUrl.takeIf { it.isNotEmpty() }
                        )
                    )
                }
            }
        }
    }


}