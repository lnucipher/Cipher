package com.example.cipher.data.local.notification

import com.example.cipher.data.local.db.AppDatabase
import com.example.cipher.data.local.db.notification.model.UnreadNotificationsEntity
import com.example.cipher.data.mappers.toUnreadNotification
import com.example.cipher.domain.models.notification.UnreadNotification
import com.example.cipher.domain.repository.notification.PushNotificationService
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PushNotificationServiceImpl @Inject constructor(
    private val database: AppDatabase
): PushNotificationService {
    override suspend fun subscribe(userId: String) {
        FirebaseMessaging.getInstance().subscribeToTopic("user_$userId")
    }

    override suspend fun unsubscribe(userId: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("user_$userId")
    }

    override suspend fun insertToUnreadNotification(
        senderId: String,
        isMuted: Boolean,
        messageText: String
    ) {
        database.unreadNotificationsDao.insert(UnreadNotificationsEntity(
            senderId = senderId,
            isMuted = isMuted,
            message = messageText
        ))
    }

    override suspend fun getAllUnreadNotifications(): Flow<List<UnreadNotification>> {
        return try {
            database.unreadNotificationsDao.getAllNotifications()
                .map { notifications -> notifications.map { it.toUnreadNotification() } }
        } catch (e: Exception) {
            flowOf(emptyList())
        }
    }

    override suspend fun setMutedStatusBySender(senderId: String, isMuted: Boolean) {
        return database.unreadNotificationsDao.updateIsMutedBySenderId(
            senderId = senderId,
            isMuted = isMuted
        )
    }

    override suspend fun deleteAllBySender(senderId: String) {
        withContext(Dispatchers.IO) {
            database.unreadNotificationsDao.deleteAllBySenderIds(listOf(senderId))
        }
    }


}