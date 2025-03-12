package com.example.cipher.ui.screens.home.settings

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cipher.domain.models.settings.Language
import com.example.cipher.domain.models.settings.NotificationSound
import com.example.cipher.domain.models.settings.NotificationVibration
import com.example.cipher.domain.models.settings.Settings
import com.example.cipher.domain.models.settings.Theme
import com.example.cipher.domain.repository.auth.AuthRepository
import com.example.cipher.domain.repository.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _settings = MutableStateFlow(Settings.getDefaultSettings())
    val settings: StateFlow<Settings> = _settings.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getSettingsFlow()
                .collect { newSettings ->
                    _settings.value = newSettings
                }
        }
    }

    fun updateNotificationEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationEnabled(isEnabled)
        }
    }

    fun updateNotificationSound(sound: NotificationSound) {
        viewModelScope.launch {
            settingsRepository.setNotificationSound(sound)
        }
    }

    fun updateNotificationVibration(vibration: NotificationVibration) {
        viewModelScope.launch {
            settingsRepository.setNotificationVibration(vibration)
        }
    }

    fun updateLanguage(language: Language) {
        viewModelScope.launch {
            settingsRepository.setLanguage(language)
        }
    }

    fun updateMessageCornersSize(size: Int) {
        viewModelScope.launch {
            settingsRepository.setMessageCornersSize(size)
        }
    }

    fun updateMessageFontSize(size: Int) {
        viewModelScope.launch {
            settingsRepository.setMessageFontSize(size)
        }
    }

    fun updateDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkThemeEnabled(isDark)
        }
    }

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    @SuppressLint("ServiceCast")
    fun logout(context: Context) {
        viewModelScope.launch {
            authRepository.logout(context)
        }
    }
}
