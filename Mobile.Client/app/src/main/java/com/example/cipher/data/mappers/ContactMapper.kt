package com.example.cipher.data.mappers

import com.example.cipher.data.local.db.contact.model.ContactEntity
import com.example.cipher.domain.models.user.User

fun User.toContactEntity(): ContactEntity {
    return ContactEntity(
        id = id,
        username = username,
        name = name,
        bio = bio,
        birthDate = birthDate,
        avatarUrl = avatarUrl,
        status = status,
        lastSeen = lastSeen
    )
}

fun ContactEntity.toUser(): User {
    return User(
        id = id,
        username = username,
        name = name,
        bio = bio,
        birthDate = birthDate,
        avatarUrl = avatarUrl,
        status = status,
        lastSeen = lastSeen
    )
}