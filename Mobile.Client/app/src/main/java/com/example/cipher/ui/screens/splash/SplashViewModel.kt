package com.example.cipher.ui.screens.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cipher.domain.repository.auth.JwtTokenManager
import com.example.cipher.ui.common.navigation.GlobalNavScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: JwtTokenManager
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<GlobalNavScreens?>(null) // Початково null
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val token = tokenManager.getAccessJwt()
            _startDestination.value = if (token == null) {
                GlobalNavScreens.AuthScreen
            } else {
                GlobalNavScreens.HomeScreen
            }
            _isLoading.value = false
        }
    }
}
