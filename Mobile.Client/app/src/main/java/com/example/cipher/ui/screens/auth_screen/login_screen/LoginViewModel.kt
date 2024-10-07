package com.example.cipher.ui.screens.auth_screen.login_screen

import androidx.lifecycle.ViewModel
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.ui.screens.auth_screen.AuthViewModel
import com.example.cipher.ui.screens.auth_screen.login_screen.models.LoginUiEvent
import com.example.cipher.ui.screens.auth_screen.models.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {

    private lateinit var authViewModel: AuthViewModel

    fun setAuthViewModel(authViewModel: AuthViewModel) {
        this.authViewModel = authViewModel
    }

    fun onEvent(event: LoginUiEvent) {
        val currentState = authViewModel.state

        when (event) {
            is LoginUiEvent.UsernameChanged -> {
                authViewModel.state = currentState.copy(
                    login = currentState.login.copy(username = event.value)
                )
            }
            is LoginUiEvent.PasswordChanged -> {
                authViewModel.state = currentState.copy(
                    login = currentState.login.copy(password = event.value)
                )
            }
            is LoginUiEvent.SingIn -> {
                authViewModel.onEvent(
                    AuthUiEvent.SignIn(
                        SignInRequest(
                            username = currentState.login.username,
                            password = currentState.login.password
                        )
                    )
                )
            }
        }
    }

}