package com.example.cipher.domain.repository.notification

import com.example.cipher.domain.models.notification.UnreadNotification
import kotlinx.coroutines.flow.Flow

interface PushNotificationService {
    suspend fun subscribe(userId: String)
    suspend fun unsubscribe(userId: String)

    suspend fun insertToUnreadNotification(senderId: String, isMuted: Boolean, messageText: String)
    suspend fun getAllUnreadNotifications(): Flow<List<UnreadNotification>>
    suspend fun setMutedStatusBySender(senderId: String, isMuted: Boolean)
    suspend fun deleteAllBySender(senderId: String)
}