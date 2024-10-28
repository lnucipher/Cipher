package com.example.cipher.data.local.db.message.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.time.LocalDateTime

@Entity("messages")
data class MessageEntity (
    @PrimaryKey val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val createdAt: LocalDateTime
)