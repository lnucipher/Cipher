package com.example.cipher.domain.repository.event

import com.microsoft.signalr.HubConnectionState

interface EventHubListener {
    val connectionId: String?
    val connectionState: HubConnectionState
    fun connectionOnClosed(onConnected: () -> Unit)
    fun startConnection()
    fun stopConnection()
    fun sendEvent(eventName: String, vararg args: Any?)
    fun <T> subscribe(eventName: String, handler: (T) -> Unit, clazz: Class<T>)
    fun unsubscribe(eventName: String)
}