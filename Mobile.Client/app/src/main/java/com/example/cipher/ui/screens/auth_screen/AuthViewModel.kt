package com.example.cipher.ui.screens.auth_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cipher.domain.models.auth.AuthResult
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest
import com.example.cipher.domain.repository.auth.AuthRepository
import com.example.cipher.domain.repository.auth.JwtTokenManager
import com.example.cipher.ui.screens.auth_screen.models.AuthState
import com.example.cipher.ui.screens.auth_screen.models.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenManager: JwtTokenManager
) : ViewModel() {

    var state by mutableStateOf(AuthState())

    private val resultChannel = Channel<AuthResult>()
    val authResult = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    fun onEvent(event: AuthUiEvent) {
        when(event) {
            is AuthUiEvent.SignUp -> {
                signUp(event.value, event.avatarUrl)
            }
            is AuthUiEvent.SignIn -> {
                signIn(event.value)
            }
        }
    }

    private fun signUp(value: SignUpRequest, avatarUrl: String?) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.signUp(value, avatarUrl)
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun signIn(value: SignInRequest) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.signIn(value)

            if (result is AuthResult.BadRequest) {
                state = state.copy(showErrorDialog = true)
            } else {
                resultChannel.send(result)
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            tokenManager.getAccessJwt()?.let {
                resultChannel.send(AuthResult.Authorized)
            }
            state = state.copy(isLoading = false)
        }
    }

}

