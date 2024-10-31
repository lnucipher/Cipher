package com.example.cipher.domain.repository.event

import com.example.cipher.domain.models.message.Message
import com.microsoft.signalr.HubConnectionState

interface EventHubListener {
    val connectionId: String?
    val connectionState: HubConnectionState
    fun startConnection(onConnected: () -> Unit)
    fun stopConnection()
    fun sendEvent(eventName: String, vararg args: Any?)
    fun subscribe(eventName: String, handler: (Message) -> Unit)
    fun unsubscribe(eventName: String)
}