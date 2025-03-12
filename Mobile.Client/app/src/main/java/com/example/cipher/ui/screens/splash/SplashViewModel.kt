package com.example.cipher.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cipher.domain.models.settings.Settings
import com.example.cipher.domain.repository.auth.JwtTokenManager
import com.example.cipher.domain.repository.settings.SettingsRepository
import com.example.cipher.ui.common.navigation.GlobalNavScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: JwtTokenManager,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<GlobalNavScreens?>(null)
    val startDestination = _startDestination.asStateFlow()

    private val _settings = MutableStateFlow(Settings.getDefaultSettings())
    val settings: StateFlow<Settings> = _settings.asStateFlow()

    init {
        loadSettings()
        authenticate()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getSettingsFlow()
                .collect { newSettings ->
                    _settings.value = newSettings
                }
        }
    }

    private fun authenticate() {
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
