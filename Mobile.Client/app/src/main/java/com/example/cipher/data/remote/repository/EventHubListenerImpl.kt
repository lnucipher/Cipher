package com.example.cipher.data.remote.repository

import com.example.cipher.domain.repository.event.EventHubListener
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionState
import javax.inject.Inject

class EventHubListenerImpl @Inject constructor(
    private val hubConnection: HubConnection
): EventHubListener {

    override val connectionId: String?
        get() = hubConnection.connectionId
    override val connectionState: HubConnectionState
        get() = hubConnection.connectionState

    override fun startConnection(startConnection: (HubConnection) -> Unit ) {
        startConnection(hubConnection)
    }

    override fun stopConnection() {
        if (hubConnection.connectionState == HubConnectionState.CONNECTED) {
            hubConnection.stop()
                .subscribe()
        }
    }

    override fun sendEvent(eventName: String, vararg args: Any?) {
        hubConnection.invoke(eventName, *args)
            .subscribe()

    }

    override fun<T> subscribe(eventName: String, handler: (T) -> Unit, clazz: Class<T>) {
        hubConnection.on(
            eventName,
            handler,
            clazz
        )
    }

    override fun unsubscribe(eventName: String) {
        hubConnection.remove(eventName)
    }
}