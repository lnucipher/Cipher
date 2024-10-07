package com.example.cipher.ui.screens.auth_screen.login_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.ui.screens.auth_screen.AuthViewModel
import com.example.cipher.ui.screens.auth_screen.login_screen.models.LoginState
import com.example.cipher.ui.screens.auth_screen.login_screen.models.LoginUiEvent
import com.example.cipher.ui.screens.auth_screen.models.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {

    private lateinit var authViewModel: AuthViewModel
    private var state by mutableStateOf(LoginState())

    fun setAuthViewModel(authViewModel: AuthViewModel) {
        this.authViewModel = authViewModel
    }

    fun onEvent (event: LoginUiEvent) {
        when(event) {
            is LoginUiEvent.UsernameChanged -> {
                state = state.copy(username = event.value)
            }
            is LoginUiEvent.PasswordChanged -> {
                state = state.copy(password = event.value)
            }
            is LoginUiEvent.SingIn -> {
                authViewModel.onEvent(AuthUiEvent.SignIn(
                    SignInRequest(
                        username = state.username,
                        password = state.password
                    )
                ))
            }
        }
    }

}