package com.example.cipher.data.local.db.contact.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cipher.domain.models.user.Status
import java.time.LocalDateTime

@Entity("contacts")
data class ContactEntity (
    @PrimaryKey val id: String,
    val username: String,
    val name: String,
    val bio: String,
    val birthDate: String,
    val avatarUrl: String,
    val status: Status,
    val lastSeen: LocalDateTime
)
