package com.example.cipher.domain.models.message

import java.time.LocalDateTime

data class Message (
    val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val createdAt: LocalDateTime
)