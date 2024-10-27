package com.example.cipher.domain.models.user

import com.example.cipher.domain.serializers.TimestampSerializer
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class User (
    val id: String,
    val username: String,
    val name: String,
    val bio: String,
    val birthDate: String,
    val avatarUrl: String,
    val status: Status,
    @Serializable(with = TimestampSerializer::class) val lastSeen: Timestamp
)
