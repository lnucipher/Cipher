package com.example.cipher.domain.models.user

import java.sql.Timestamp
import java.time.LocalDate

data class User(
    val id: String,
    val username: String,
    val passwordHash: String,
    val name: String,
    val bio: String,
    val birthData: LocalDate,
    val avatarUrl: String,
    val status: Status,
    val lastSeen: Timestamp
)