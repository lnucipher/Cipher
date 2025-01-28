package com.example.cipher.ui.screens.home.chats

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import coil.ImageLoader
import com.example.cipher.domain.models.notification.UnreadNotification
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.contact.ContactRepository
import com.example.cipher.domain.repository.notification.PushNotificationService
import com.example.cipher.domain.repository.user.UserRepository
import com.example.cipher.ui.screens.home.chats.models.ChatsMultiSelectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val userRepository: UserRepository,
    private val pushNotificationService: PushNotificationService,
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
    val contactPagingDataFlow: Flow<PagingData<User>> = localUser
        .filterNot { it.id.isEmpty() }
        .distinctUntilChanged()
        .flatMapLatest { localUser -> contactRepository.getContactList(localUser.id).cachedIn(viewModelScope) }

    private var _searchResults: MutableStateFlow<List<Pair<User, Boolean>>> = MutableStateFlow(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private var _multiSelectionState: MutableState<ChatsMultiSelectionState> = mutableStateOf(ChatsMultiSelectionState())
    val multiSelectionState: State<ChatsMultiSelectionState> = _multiSelectionState

    private var _unreadNotifications = mutableStateOf<List<UnreadNotification>>(emptyList())

    init {
        updateUnreadNotificationState()
    }

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

    private fun setMultiSelectionEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _multiSelectionState.value = _multiSelectionState.value.copy(
                isMultiSelectionEnabled = enabled,
                itemsSelected = if (!enabled) emptySet() else _multiSelectionState.value.itemsSelected
            )
        }
    }

    fun disableMultiSelection() {
        setMultiSelectionEnabled(false)
    }

    fun enableMultiSelection() {
        setMultiSelectionEnabled(true)
    }

    fun toggleItemSelection(itemId: String) {
        viewModelScope.launch {
            val currentState = _multiSelectionState.value
            if (currentState.isMultiSelectionEnabled) {
                val updatedItemsSelected = if (currentState.itemsSelected.contains(itemId)) {
                    currentState.itemsSelected - itemId
                } else {
                    currentState.itemsSelected + itemId
                }

                _multiSelectionState.value = currentState.copy(itemsSelected = updatedItemsSelected)
            }

            if (_multiSelectionState.value.itemsSelected.isEmpty()) setMultiSelectionEnabled(false)
        }
    }

    fun deleteContacts(contactIds: Set<String>) {
        viewModelScope.launch {
            contactRepository.deleteContact(localUser.value.id, contactIds)
        }
    }

    private fun updateUnreadNotificationState() {
        viewModelScope.launch {
            pushNotificationService.getAllUnreadNotifications().collect { notifications ->
                _unreadNotifications.value = notifications
            }
        }
    }

    fun getIsMutedBySenderId(senderId: String): Boolean {
        return _unreadNotifications.value.find { it.senderId == senderId }?.isMuted ?: false
    }

    fun countNotificationsBySenderId(senderId: String): Int {
        return _unreadNotifications.value.count { it.senderId == senderId }
    }

    fun deleteAllUnreadNotificationBySenderId(senderId: String) {
        viewModelScope.launch {
            pushNotificationService.deleteAllBySender(senderId)
        }
    }
}