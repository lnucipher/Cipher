package com.example.cipher.data.local.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {


        Log.d("TAG", message.notification?.body.toString())
        Log.d("TAG", message.rawData.toString())

        super.onMessageReceived(message)
    }
}
