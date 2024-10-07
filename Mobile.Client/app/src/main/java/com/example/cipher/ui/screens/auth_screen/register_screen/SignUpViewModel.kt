package com.example.cipher.ui.screens.auth_screen.register_screen

import androidx.lifecycle.ViewModel
import com.example.cipher.domain.models.auth.SignUpRequest
import com.example.cipher.ui.screens.auth_screen.AuthViewModel
import com.example.cipher.ui.screens.auth_screen.models.AuthUiEvent
import com.example.cipher.ui.screens.auth_screen.register_screen.models.SignUpUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(): ViewModel() {

    private lateinit var authViewModel: AuthViewModel

    fun setAuthViewModel(authViewModel: AuthViewModel) {
        this.authViewModel = authViewModel
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
                authViewModel.onEvent(
                    AuthUiEvent.SignUp(
                        SignUpRequest(
                            username = currentState.signUp.username,
                            password = currentState.signUp.password,
                            name = currentState.signUp.name,
                            bio = currentState.signUp.bio,
                            birthDate = LocalDate.now(), // LocalDate.parse(currentState.signUp.birthDate)
                            avatarUrl = currentState.signUp.avatarUrl
                        )
                    )
                )
            }
        }
    }


}