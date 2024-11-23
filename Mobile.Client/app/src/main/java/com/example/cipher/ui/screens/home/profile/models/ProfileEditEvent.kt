package com.example.cipher.ui.screens.home.profile.models

sealed class ProfileEditEvent {
    data class UsernameChanged(val value: String) : ProfileEditEvent()
    data class NamedChanged(val value: String) : ProfileEditEvent()
    data class BioChanged(val value: String) : ProfileEditEvent()
    data class BirthDateChanged(val value: String) : ProfileEditEvent()
    data class AvatarUrlChanged(val value: String) : ProfileEditEvent()
    data object UpdateFields : ProfileEditEvent()
    data object UpdateAvatar : ProfileEditEvent()
}