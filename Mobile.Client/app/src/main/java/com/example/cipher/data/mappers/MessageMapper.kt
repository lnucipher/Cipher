package com.example.cipher.data.mappers

import com.example.cipher.data.local.db.message.model.MessageEntity
import com.example.cipher.domain.models.message.Message

fun Message.toMessageEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        senderId = senderId,
        receiverId = receiverId,
        text = text,
        createdAt = createdAt
    )
}

fun MessageEntity.toMessage(): Message {
    return Message(
        id = id,
        senderId = senderId,
        receiverId = receiverId,
        text = text,
        createdAt = createdAt
    )
}