package com.example.cipher.data.local.notification

import android.util.Log
import com.example.cipher.domain.repository.notification.PushNotificationService
import com.google.firebase.messaging.FirebaseMessaging

class PushNotificationServiceImpl: PushNotificationService {

    override suspend fun subscribe(userId: String) {
        FirebaseMessaging.getInstance().subscribeToTopic("user_$userId".lowercase()).addOnCompleteListener { Log.d("TAG", "Here") }
    }

}