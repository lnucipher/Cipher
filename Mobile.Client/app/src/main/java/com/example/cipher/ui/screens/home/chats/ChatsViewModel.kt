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
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.contact.ContactRepository
import com.example.cipher.domain.repository.message.MessageRepository
import com.example.cipher.domain.repository.user.LocalUserManager
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
    private val userManager: LocalUserManager,
    private val eventService: EventSubscriptionServiceImpl,
    private val messageRepository: MessageRepository
): ViewModel() {

    private val subscriptions = mutableListOf<EventResourceSubscription>()

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

    private var _searchResults: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val searchResults = _searchResults.asStateFlow()

    init {
        initializeLocalUser()
        setupWebSocketConnection()
    }

    private fun initializeLocalUser() {
        viewModelScope.launch {
            try {
                val user = userManager.getUser()
                _localUser.update { user }
            } catch (_: Exception) {}
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

    private fun setupWebSocketConnection() {
        viewModelScope.launch {
            eventService.connectToHub {
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
            viewModelScope.launch {
                contactRepository.updateContactStatus(data as String, Status.ONLINE)
            }
        })
        val userDisconnectedSubscription = EventResourceSubscription("UserDisconnected", EventSubscriptionType.USER_DISCONNECTED , callback = { data ->
            viewModelScope.launch {
                contactRepository.updateContactStatus(data as String, Status.OFFLINE)
            }
        })
        subscriptions.add(messageReceivedSubscription)
        subscriptions.add(userConnectedSubscription)
        subscriptions.add(userDisconnectedSubscription)
        eventService.subscribe(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        eventService.unsubscribe(subscriptions)
    }

}