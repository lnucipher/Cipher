package com.example.cipher.data.remote.repository

import android.util.Log
import com.example.cipher.domain.models.event.EventResourceSubscription
import com.example.cipher.domain.models.event.EventSubscriptionType
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.repository.event.EventHubListener
import com.example.cipher.domain.repository.event.EventSubscriptionService
import com.microsoft.signalr.HubConnectionState
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapter
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EventSubscriptionServiceImpl @Inject constructor(
    private val eventHubListener: EventHubListener,
    private val moshi: Moshi
): EventSubscriptionService {

    private val eventResourceSubscription = mutableListOf<EventResourceSubscription>()

    private fun subscribeOnUserConnected() {
        eventHubListener.subscribe("UserConnected") {
        }
    }

    suspend fun connectToHub(userId: String, contactIds: List<String>, onConnected: () -> Unit) {
        suspendCoroutine { continuation ->
            onConnected()
            eventHubListener.startConnection {
                connectEvent(userId, contactIds)
                continuation.resume(Unit)
            }
        }
    }

    private fun connectEvent(localUserId: String, contactIds: List<String>) {
        val listType = Types.newParameterizedType(List::class.java, String::class.java)
        val contactIdsJson = moshi.adapter<List<String>>(listType)

        eventHubListener.sendEvent("Connect", localUserId, contactIdsJson)
        Log.i("TAG", "Connect method called with userId: $localUserId and contacts: $contactIds")
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun subscribeOnReceive() {
        Log.i("TAG", "Subscribing to ReceiveMessage")
        eventHubListener.subscribe("ReceiveMessage") { event ->
            Log.i("TAG", "Received event: $event")
            Log.i("TAG", "HubConnection State: ${eventHubListener.connectionState}")
            if (eventHubListener.connectionState != HubConnectionState.CONNECTED) {
                Log.w("TAG", "Connection is closed. Reconnecting...")
            } else {
                Log.i("TAG", "Received event: $event")
            }
        }
    }

    override fun subscribe(subscriptionsAdd: List<EventResourceSubscription>) {
        subscriptionsAdd.forEach { resourceSubscription ->
            if (!eventResourceSubscription.contains(resourceSubscription)) {
                eventResourceSubscription.add(resourceSubscription)
                Log.i("TAG", "Added subscription: ${resourceSubscription.resourceName}")
                subscribeToEvent(resourceSubscription)
            } else {
                Log.i("TAG", "Already subscribed to: ${resourceSubscription.resourceName}")
            }
        }

    }

    override fun unsubscribe(subscriptionsRemove: List<EventResourceSubscription>) {
        subscriptionsRemove.forEach { resourceSubscription ->
            if (eventResourceSubscription.contains(resourceSubscription)) {
                eventResourceSubscription.remove(resourceSubscription)
                Log.i("TAG", "Removed subscription: ${resourceSubscription.resourceName}")
                unsubscribeFromEvent(resourceSubscription)
            } else {
                Log.i("TAG", "Not subscribed to: ${resourceSubscription.resourceName}")
            }
        }
    }

    private fun subscribeToEvent(subscription: EventResourceSubscription) {
        when (subscription.type) {
            EventSubscriptionType.RECEIVE_MESSAGE -> subscribeOnReceive()
            EventSubscriptionType.USER_CONNECTED -> subscribeOnUserConnected()
        }
    }

    private fun unsubscribeFromEvent(subscription: EventResourceSubscription) {
        eventHubListener.unsubscribe(subscription.resourceName)
    }

}

