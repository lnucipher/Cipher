package com.example.cipher.ui.screens.auth_screen.login_screen

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.ui.screens.auth_screen.AuthViewModel
import com.example.cipher.ui.screens.auth_screen.composable.AuthValidation
import com.example.cipher.ui.screens.auth_screen.login_screen.models.LoginUiEvent
import com.example.cipher.ui.screens.auth_screen.models.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {

    private lateinit var authViewModel: AuthViewModel
    var validationState by mutableStateOf(mutableMapOf<AuthValidation, Boolean>())

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
                if (validationState.values.all { it }) {
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

}