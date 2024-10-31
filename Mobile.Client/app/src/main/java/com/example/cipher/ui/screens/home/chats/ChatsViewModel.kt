package com.example.cipher.ui.screens.home.chats

import android.util.Log
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
    init {
//        initializeLocalUser()
        setupWebSocketConnection("3fa85f64-5717-4562-b3fc-2c963f66afa6")
    }

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

    private fun setupWebSocketConnection(userId: String) {
        viewModelScope.launch {
            val contactIds = listOf("3fa85f64-5717-4562-b3fc-2c963f66afa5")
            eventService.connectToHub(userId, contactIds) {
                setupEventListeners()
            }
        }
    }

    private fun setupEventListeners() {
        val messageReceivedSubscription = EventResourceSubscription("ReceiveMessage", EventSubscriptionType.RECEIVE_MESSAGE , callback = { data ->
            viewModelScope.launch {
                Log.i("TAG", "ViewModel ${data.toString()}")
                messageRepository.saveMessage(data as Message)
            }
        })
        subscriptions.add(messageReceivedSubscription)
        eventService.subscribe(subscriptions)

//        viewModelScope.launch {
//            delay(10000)
//            val message = Message(
//                senderId = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
//                receiverId = "3fa85f64-5717-4562-b3fc-2c963f66afa5",
//                text = "huy",
//                id = "asdasdasdasd",
//                createdAt = LocalDateTime.now()
//            )
//            for (i in 1..5) {
//                messageRepository.saveMessage(message.copy(id = "${message.id}$i"))
//                delay(1000)
//            }
//        }
    }

    override fun onCleared() {
        super.onCleared()
        eventService.unsubscribe(subscriptions)
    }

}