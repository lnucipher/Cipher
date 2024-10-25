package com.example.cipher.domain.models.message

data class PagerMessageRequest  (
    val offset: Int,
    val size: Int,
    val messageList: List<Message>
)