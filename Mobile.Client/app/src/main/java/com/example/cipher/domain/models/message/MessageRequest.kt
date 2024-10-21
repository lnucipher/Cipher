package com.example.cipher.domain.models.message

data class MessageRequest (
    val senderId: String,
    val receiverId: String,
    val text: String
)