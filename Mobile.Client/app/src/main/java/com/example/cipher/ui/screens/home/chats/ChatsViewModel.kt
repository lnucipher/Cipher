package com.example.cipher.ui.screens.home.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cipher.data.remote.repository.EventSubscriptionServiceImpl
import com.example.cipher.domain.models.event.EventResourceSubscription
import com.example.cipher.domain.models.event.EventSubscriptionType
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.contact.ContactRepository
import com.example.cipher.domain.repository.message.MessageRepository
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
    contactRepository: ContactRepository,
    private val userRepository: UserRepository,
    private val userManager: LocalUserManager,
    private val eventService: EventSubscriptionServiceImpl,
    private val messageRepository: MessageRepository
): ViewModel() {

    private val subscriptions = mutableListOf<EventResourceSubscription>()

    val contactPagingDataFlow: Flow<PagingData<User>> = contactRepository.getContactList()
        .cachedIn(viewModelScope)

    private val _localUser: MutableStateFlow<LocalUser> = MutableStateFlow(
        LocalUser(
            id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
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

    init {
        initializeLocalUser()
    }

    private fun initializeLocalUser() {
        viewModelScope.launch {
            try {
                val user = userManager.getUser()
//                _localUser.update { user }
                setupWebSocketConnection(localUser.value.id)
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

    private fun setupWebSocketConnection(userId: String) {
        viewModelScope.launch {
            val contactIds = emptyList<String>()
            eventService.connectToHub(userId, contactIds) {
                setupEventListeners()
            }
        }
    }

    private fun setupEventListeners() {
        val messageReceivedSubscription = EventResourceSubscription("ReceiveMessage", EventSubscriptionType.RECEIVE_MESSAGE , callback = { data ->
            viewModelScope.launch {
                messageRepository.saveMessage(data as Message)
            }
        })
        val userConnectedSubscription = EventResourceSubscription("UserConnected", EventSubscriptionType.USER_CONNECTED , callback = { data ->
        })
        subscriptions.add(messageReceivedSubscription)
        subscriptions.add(userConnectedSubscription)
        eventService.subscribe(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        eventService.unsubscribe(subscriptions)
    }

}