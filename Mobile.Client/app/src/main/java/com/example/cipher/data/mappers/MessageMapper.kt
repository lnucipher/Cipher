package com.example.cipher.data.mappers

import com.example.cipher.data.local.db.message.model.MessageEntity
import com.example.cipher.data.remote.api.dto.MessageHubDto
import com.example.cipher.domain.models.message.Message
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

fun MessageHubDto.toMessage(): Message {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    return Message(
        id = id,
        senderId = senderId,
        receiverId = receiverId,
        text = text,
        createdAt = LocalDateTime.parse(createdAt, formatter)
    )
}
