package com.example.cipher.data.remote.repository

import com.example.cipher.data.mappers.toMessage
import com.example.cipher.data.remote.api.dto.MessageHubDto
import com.example.cipher.domain.models.event.EventResourceSubscription
import com.example.cipher.domain.models.event.EventSubscriptionType
import com.example.cipher.domain.repository.event.EventHubListener
import com.example.cipher.domain.repository.event.EventSubscriptionService
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventSubscriptionServiceImpl @Inject constructor(
    private val eventHubListener: EventHubListener
): EventSubscriptionService {

    private val eventResourceSubscription = mutableListOf<EventResourceSubscription>()

    fun connectToHub(onConnected: () -> Unit) {
        onConnected()
        startConnection()
    }

    @Volatile
    private var isReconnecting = false

    private fun startConnection() {
        eventHubListener.startConnection { hubConnection ->
            if (hubConnection.connectionState == HubConnectionState.DISCONNECTED) {
                hubConnection.start()
                    .doOnError {
                        handleDisconnection()
                    }
                    .onErrorComplete{ true }
                    .subscribe()

                hubConnection.onClosed {
                    handleDisconnection()
                }
            }
        }
    }

    private fun handleDisconnection() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!isReconnecting) {
                isReconnecting = true
                reconnect()
                isReconnecting = false
            }
        }
    }

    private suspend fun reconnect() {
        while (isReconnecting) {
            resubscribeToEvents()
            startConnection()
            delay(3000L)
        }
    }

    private fun resubscribeToEvents() {
        if (eventHubListener.connectionState == HubConnectionState.CONNECTED) {
            eventResourceSubscription.forEach { subscription ->
                subscribeToEvent(subscription)
            }
        }
    }

    private fun subscribeOnUserDisconnected(subscription: EventResourceSubscription) {
        eventHubListener.subscribe(
            "UserDisconnected",
            handler = { eventData ->
                if (eventHubListener.connectionState == HubConnectionState.CONNECTED) {
                    subscription.callback(eventData)
                }
            },
            clazz = String::class.java
        )
    }

    private fun subscribeOnUserConnected(subscription: EventResourceSubscription) {
        eventHubListener.subscribe(
            "UserConnected",
            handler = { eventData ->
                if (eventHubListener.connectionState == HubConnectionState.CONNECTED) {
                    subscription.callback(eventData)
                }
            },
            clazz = String::class.java
        )
    }

    private fun subscribeOnReceive(subscription: EventResourceSubscription) {
        eventHubListener.subscribe(
            "ReceiveMessage",
            handler = { eventData ->
                if (eventHubListener.connectionState == HubConnectionState.CONNECTED) {
                    subscription.callback(eventData.toMessage())
                }
            },
            clazz = MessageHubDto::class.java
        )
    }

    override fun subscribe(subscriptionsAdd: List<EventResourceSubscription>) {
        subscriptionsAdd.forEach { resourceSubscription ->
            if (!eventResourceSubscription.contains(resourceSubscription)) {
                eventResourceSubscription.add(resourceSubscription)
                subscribeToEvent(resourceSubscription)
            }
        }

    }

    override fun unsubscribe(subscriptionsRemove: List<EventResourceSubscription>) {
        subscriptionsRemove.forEach { resourceSubscription ->
            if (eventResourceSubscription.contains(resourceSubscription)) {
                eventResourceSubscription.remove(resourceSubscription)
                unsubscribeFromEvent(resourceSubscription)
            }
        }
        eventHubListener.stopConnection()
    }

    private fun subscribeToEvent(subscription: EventResourceSubscription) {
        when (subscription.type) {
            EventSubscriptionType.RECEIVE_MESSAGE -> subscribeOnReceive(subscription)
            EventSubscriptionType.USER_CONNECTED -> subscribeOnUserConnected(subscription)
            EventSubscriptionType.USER_DISCONNECTED -> subscribeOnUserDisconnected(subscription)
        }
    }

    private fun unsubscribeFromEvent(subscription: EventResourceSubscription) {
        eventHubListener.unsubscribe(subscription.resourceName)
    }

}

