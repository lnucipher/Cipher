package com.example.cipher.domain.models.message

data class PagerMessageRequest  (
    val senderId: String,
    val receiverId: String,
    val page: Int,
    val pageSize: Int
)