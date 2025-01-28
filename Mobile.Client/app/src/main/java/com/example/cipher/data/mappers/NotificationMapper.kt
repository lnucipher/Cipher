package com.example.cipher.data.mappers

import com.example.cipher.data.local.db.notification.model.UnreadNotificationsEntity
import com.example.cipher.domain.models.notification.UnreadNotification

fun UnreadNotificationsEntity.toUnreadNotification(): UnreadNotification {
    return UnreadNotification(
        senderId = senderId,
        isMuted = isMuted,
        message = message
    )
}