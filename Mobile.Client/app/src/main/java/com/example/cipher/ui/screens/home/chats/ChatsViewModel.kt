package com.example.cipher.ui.screens.home.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import coil.ImageLoader
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.contact.ContactRepository
import com.example.cipher.domain.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val userRepository: UserRepository,
    val imageLoader: ImageLoader
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val contactPagingDataFlow: Flow<PagingData<User>> = localUser.flatMapLatest { localUser ->
        if (localUser.id.isEmpty()) {
            flowOf(PagingData.empty())
        } else {
            contactRepository.getContactList(localUser.id).cachedIn(viewModelScope)
        }
    }

    private var _searchResults: MutableStateFlow<List<Pair<User, Boolean>>> = MutableStateFlow(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun setLocalUser(localUser: LocalUser) {
        viewModelScope.launch {
            _localUser.update { localUser }
        }
    }

    fun searchUsers(searchedUsername: String) {
        viewModelScope.launch {
            _searchResults.update { userRepository.searchUsers(
                requestorId = localUser.value.id,
                searchedUsername = searchedUsername
            ) }
        }
    }

    fun clearSearchResults() {
        _searchResults.update { emptyList() }
    }

    fun addContact(user: User) {
        viewModelScope.launch {
            contactRepository.addContact(localUser.value.id, user)
        }
    }

    fun deleteContact(contactId: String) {
        viewModelScope.launch {
            contactRepository.deleteContact(localUser.value.id, contactId)
        }
    }

}