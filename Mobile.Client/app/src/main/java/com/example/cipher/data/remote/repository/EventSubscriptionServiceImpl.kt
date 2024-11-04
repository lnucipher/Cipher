package com.example.cipher.data.remote.repository

import com.example.cipher.data.mappers.toMessage
import com.example.cipher.data.remote.api.dto.MessageHubDto
import com.example.cipher.domain.models.event.EventResourceSubscription
import com.example.cipher.domain.models.event.EventSubscriptionType
import com.example.cipher.domain.repository.event.EventHubListener
import com.example.cipher.domain.repository.event.EventSubscriptionService
import com.microsoft.signalr.HubConnectionState
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EventSubscriptionServiceImpl @Inject constructor(
    private val eventHubListener: EventHubListener
): EventSubscriptionService {

    private val eventResourceSubscription = mutableListOf<EventResourceSubscription>()

    suspend fun connectToHub(userId: String, contactIds: List<String>, onConnected: () -> Unit) {
        suspendCoroutine { continuation ->
            onConnected()
            continuation.resume(Unit)
            eventHubListener.startConnection {
                connectEvent(userId, contactIds)
            }
        }
    }

    private fun connectEvent(localUserId: String, contactIds: List<String>) {
        eventHubListener.sendEvent("Connect", localUserId)
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
        }
    }

    private fun unsubscribeFromEvent(subscription: EventResourceSubscription) {
        eventHubListener.unsubscribe(subscription.resourceName)
    }

}

