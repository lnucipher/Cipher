package com.example.cipher.ui.screens.home.settings

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
//    private val authRepository: AuthRepository
): ViewModel() {

    private val _localUser: MutableStateFlow<LocalUser> = MutableStateFlow(
        LocalUser(
            id = "",
            username = "",
            name = "",
            birthDate = "",
            bio = "",
            avatarUrl = ""
        )
    )
    val localUser = _localUser.asStateFlow()

    fun setLocalUser(localUser: LocalUser) {
        viewModelScope.launch {
            _localUser.update { localUser }
        }
    }

    @SuppressLint("ServiceCast")
    fun logout(context: Context) {
//        viewModelScope.launch {
//            authRepository.logout(context)
//        }
    }
}