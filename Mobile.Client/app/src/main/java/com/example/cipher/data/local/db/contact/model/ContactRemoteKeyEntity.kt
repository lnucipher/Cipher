package com.example.cipher.data.local.db.contact.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("contact_remote_keys")
class ContactRemoteKeyEntity (
    @PrimaryKey val id: String,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean,
    val pageNumber: Int
)