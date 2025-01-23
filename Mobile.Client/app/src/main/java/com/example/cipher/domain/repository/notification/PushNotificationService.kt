package com.example.cipher.domain.repository.notification

interface PushNotificationService {
    suspend fun subscribe(userId: String)
    suspend fun unsubscribe(userId: String)
}