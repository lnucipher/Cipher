package com.example.cipher.ui.screens.home.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.contact.GetContactList
import com.example.cipher.domain.repository.user.LocalUserManager
import com.example.cipher.domain.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    getContactList: GetContactList,
    private val userRepository: UserRepository,
    private val userManager: LocalUserManager
): ViewModel() {

    init {
        initializeLocalUser()
    }

    val contactPagingDataFlow: Flow<PagingData<User>> = getContactList()
        .cachedIn(viewModelScope)

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

    private var _searchResults: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private fun initializeLocalUser() {
        viewModelScope.launch {
            try {
                val user = userManager.getUser()
                _localUser.update { user }
            } catch (_: Exception) {}
        }
    }

    fun getUsersByUsername(username: String) {
        viewModelScope.launch {
            _searchResults.update { userRepository.getUsersByUsername(username) }
        }
    }

    fun clearSearchResults() {
        _searchResults.update { emptyList() }
    }

}