package com.example.cipher.data.local.db.notification.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("unread_notifications")
data class UnreadNotificationsEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val senderId: String,
    val message: String,
    val isMuted: Boolean = false
)