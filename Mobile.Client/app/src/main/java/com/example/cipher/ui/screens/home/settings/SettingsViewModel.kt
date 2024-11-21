package com.example.cipher.ui.screens.home.settings

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cipher.domain.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    @SuppressLint("ServiceCast")
    fun logout(context: Context) {
        viewModelScope.launch {
            authRepository.logout(context)
        }
    }
}