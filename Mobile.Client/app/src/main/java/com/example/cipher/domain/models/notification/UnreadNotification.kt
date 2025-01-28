package com.example.cipher.domain.models.notification

data class UnreadNotification (
    val senderId: String,
    val message: String,
    val isMuted: Boolean
)