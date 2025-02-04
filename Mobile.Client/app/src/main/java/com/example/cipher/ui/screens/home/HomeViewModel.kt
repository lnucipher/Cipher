package com.example.cipher.ui.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.example.cipher.data.remote.repository.EventSubscriptionServiceImpl
import com.example.cipher.domain.models.event.EventResourceSubscription
import com.example.cipher.domain.models.event.EventSubscriptionType
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.repository.auth.AuthRepository
import com.example.cipher.domain.repository.contact.ContactRepository
import com.example.cipher.domain.repository.message.MessageRepository
import com.example.cipher.domain.repository.notification.PushNotificationService
import com.example.cipher.domain.repository.user.LocalUserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userManager: LocalUserManager,
    private val authRepository: AuthRepository,
    private val contactRepository: ContactRepository,
    private val messageRepository: MessageRepository,
    private val eventService: EventSubscriptionServiceImpl,
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

    private val subscriptions = mutableListOf<EventResourceSubscription>()

    init {
        initializeLocalUser()
        setupWebSocketConnection()
    }

    private fun initializeLocalUser() {
        viewModelScope.launch {
            try {
                val user = userManager.getUser()
                _localUser.update { user }
                subscribeOnNotifications(user.id)
            } catch (_: Exception) {}
        }
    }

    private suspend fun subscribeOnNotifications(userId: String) {
        pushNotificationService.subscribe(userId)
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
                contactRepository.updateContactStatus(data as String, Status.ONLINE, LocalDateTime.now())
            }
        })
        val userDisconnectedSubscription = EventResourceSubscription("UserDisconnected", EventSubscriptionType.USER_DISCONNECTED , callback = { data ->
            viewModelScope.launch {
                contactRepository.updateContactStatus(data as String, Status.OFFLINE, LocalDateTime.now())
            }
        })
        subscriptions.add(messageReceivedSubscription)
        subscriptions.add(userConnectedSubscription)
        subscriptions.add(userDisconnectedSubscription)
        eventService.subscribe(subscriptions)
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            authRepository.logout(context)
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventService.unsubscribe(subscriptions)
    }

}