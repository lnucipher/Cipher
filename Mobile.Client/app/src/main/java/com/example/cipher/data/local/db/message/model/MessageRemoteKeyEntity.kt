package com.example.cipher.data.local.db.message.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("message_remote_keys")
class MessageRemoteKeyEntity (
    @PrimaryKey val id: String,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean,
    val pageNumber: Int
)