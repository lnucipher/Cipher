package com.example.cipher.domain.models.user

import com.example.cipher.domain.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class User (
    val id: String,
    val username: String,
    val name: String,
    val bio: String,
    val birthDate: String,
    val avatarUrl: String,
    val status: Status,
    @Serializable(with = LocalDateTimeSerializer::class) val lastSeen: LocalDateTime
)
