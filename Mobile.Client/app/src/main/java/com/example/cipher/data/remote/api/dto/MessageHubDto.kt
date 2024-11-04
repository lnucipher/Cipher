package com.example.cipher.data.remote.api.dto

data class MessageHubDto (
    val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val createdAt: String
)