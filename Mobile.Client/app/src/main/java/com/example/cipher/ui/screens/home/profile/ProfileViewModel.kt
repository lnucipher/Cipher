package com.example.cipher.ui.screens.home.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.example.cipher.data.remote.api.dto.update.UpdateProfileRequestDto
import com.example.cipher.domain.models.user.EditResult
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.repository.user.LocalUserManager
import com.example.cipher.domain.repository.user.UserRepository
import com.example.cipher.ui.screens.auth.utils.AuthValidation
import com.example.cipher.ui.screens.home.profile.models.ProfileEditEvent
import com.example.cipher.ui.screens.home.profile.models.ProfileEditState
import com.example.cipher.ui.screens.home.profile.models.ProfileEditValidationState
import com.example.cipher.ui.screens.home.profile.models.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userManager: LocalUserManager,
    private val repository: UserRepository,
    val imageLoader: ImageLoader
): ViewModel() {
    private val _localUser: MutableState<LocalUser> = mutableStateOf(
        LocalUser(
            id = "",
            username = "",
            name = "",
            birthDate = "",
            bio = "",
            avatarUrl = ""
        )
    )
    val localUser = _localUser

    private val resultChannel = Channel<EditResult>()
    val editResult = resultChannel.receiveAsFlow()

    var state by mutableStateOf(ProfileState())
    var validationState by mutableStateOf(ProfileEditValidationState())

    fun setLocalUser(localUser: LocalUser) {
        viewModelScope.launch {
            _localUser.value = localUser
            setupEditState(localUser)
        }
    }

    private fun updateLocalUser() {
        viewModelScope.launch {
            try {
                val user = userManager.getUser()
                _localUser.value = user
                setupEditState(user)
            } catch (_: Exception) {}
        }
    }

    private fun setupEditState(localUser: LocalUser) {
        state = state.copy(
            profileEditState = ProfileEditState(
                username = localUser.username,
                name = localUser.name,
                bio = localUser.bio,
                birthDate = localUser.birthDate
            )
        )
    }

    private fun validateFields(): Boolean {
        with(state.profileEditState) {
            validationState = validationState.copy(
                isUsernameValid = AuthValidation.UsernameValidation.validate(username),
                isNameValid = AuthValidation.EmptyValidation.validate(name),
                isBioValid = AuthValidation.BioValidation.validate(bio),
                isBirthDateValid = AuthValidation.BirthDateValidation.validate(birthDate)
            )
        }
        return validationState.run {
            isUsernameValid &&
                    isNameValid &&
                    isBioValid &&
                    isBirthDateValid
        }
    }

    fun onClear() {
        setupEditState(localUser.value)
    }

    private fun updateFields(request: UpdateProfileRequestDto) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.updateProfile(request)
            resultChannel.send(result)
            state = state.copy(isLoading = false)
            updateLocalUser()
        }
    }

    private fun updateAvatar(userId: String, avatarUrl: String?) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.updateAvatar(
                userId = userId,
                avatarUrl = avatarUrl
            )
            resultChannel.send(result)
            state = state.copy(isLoading = false)
            updateLocalUser()
        }
    }

    fun onEvent(event: ProfileEditEvent) {
        val currentEditState = state.profileEditState
        when (event) {
            is ProfileEditEvent.UsernameChanged -> {
                state = state.copy(profileEditState = currentEditState.copy(username = event.value))
            }

            is ProfileEditEvent.NamedChanged -> {
                state = state.copy(profileEditState = currentEditState.copy(name = event.value))
            }

            is ProfileEditEvent.BioChanged -> {
                state = state.copy(profileEditState = currentEditState.copy(bio = event.value))
            }

            is ProfileEditEvent.BirthDateChanged -> {
                state = state.copy(profileEditState = currentEditState.copy(birthDate = event.value))
            }

            is ProfileEditEvent.AvatarUrlChanged -> {
                state = state.copy(profileEditState = currentEditState.copy(avatarUrl = event.value))
            }

            is ProfileEditEvent.UpdateFields -> {
                if (validateFields()) {
                    with(state.profileEditState) {
                        if (localUser.value.id.isNotBlank()) {
                            val id = _localUser.value.id
                            if (avatarUrl.isNotBlank()) {
                                updateAvatar(id, avatarUrl)
                            }
                            val updatedFields = UpdateProfileRequestDto(
                                id = id,
                                username = if (username == localUser.value.username) null else username,
                                name = if (name == localUser.value.name) null else name,
                                bio = if (bio == localUser.value.bio) null else bio,
                                birthDate = if (birthDate == localUser.value.birthDate) null else birthDate
                            )

                            if (updatedFields.username != null || updatedFields.name != null || updatedFields.bio != null || updatedFields.birthDate != null) {
                                updateFields(updatedFields)
                            }
                        }
                    }
                }
            }

            is ProfileEditEvent.UpdateAvatar -> {
                updateAvatar(
                    userId = localUser.value.id,
                    avatarUrl = state.profileEditState.avatarUrl
                )
            }
        }
    }
}