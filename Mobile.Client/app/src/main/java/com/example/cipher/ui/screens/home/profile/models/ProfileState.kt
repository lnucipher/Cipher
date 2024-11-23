package com.example.cipher.ui.screens.home.profile.models

data class ProfileState(
    val profileEditState: ProfileEditState = ProfileEditState(),
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    var showErrorDialog: Boolean = false,
    var dialogMessage: String = "",
    var dialogTitle: String = ""
)

data class ProfileEditState(
    val username: String = "",
    val name: String = "",
    val bio: String = "",
    val birthDate: String = "",
    val avatarUrl: String = "",
)

data class ProfileEditValidationState(
    val isUsernameValid: Boolean = true,
    val isNameValid: Boolean = true,
    val isBioValid: Boolean = true,
    val isBirthDateValid: Boolean = true
)