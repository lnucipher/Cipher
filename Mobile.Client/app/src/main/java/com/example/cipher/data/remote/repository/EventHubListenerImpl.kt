package com.example.cipher.data.remote.repository

import android.util.Log
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.repository.event.EventHubListener
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionState
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

class EventHubListenerImpl @Inject constructor(
    private val hubConnection: HubConnection
): EventHubListener {

    override val connectionId: String?
        get() = hubConnection.connectionId
    override val connectionState: HubConnectionState
        get() = hubConnection.connectionState

    override fun startConnection(onConnected: () -> Unit) {
        if (hubConnection.connectionState == HubConnectionState.DISCONNECTED) {
            hubConnection.start()
                .doOnComplete {
                    Log.i("TAG", "Connection started with ID: ${hubConnection.connectionId}")
                    onConnected()
                }
                .doOnError { error ->
                    Log.e("TAG", "Failed to start connection: ${error.message}")
                }
                .subscribe()
        }
    }

    override fun stopConnection() {
        if (hubConnection.connectionState == HubConnectionState.CONNECTED) {
            hubConnection.stop()
                .doOnComplete {
                    Log.i("TAG", "Connection stopped.")
                }
                .doOnError { error ->
                    Log.e("TAG", "Failed to stop connection: ${error.message}")
                }
                .subscribe()
        }
    }

    override fun sendEvent(eventName: String, vararg args: Any?) {
        hubConnection.send(eventName, args)
    }

    override fun subscribe(eventName: String, handler: (Message) -> Unit) {
        Log.i("TAG", "Listener")
        hubConnection.on(
            eventName,
            handler,
            Any::class.java
        )
    }

    override fun unsubscribe(eventName: String) {
        hubConnection.remove(eventName)
    }
}