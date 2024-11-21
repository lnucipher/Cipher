package com.example.cipher.data.mappers

import com.example.cipher.data.remote.api.dto.AuthResponseDto
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import java.time.LocalDateTime

fun AuthResponseDto.toLocalUser() : LocalUser {
    return LocalUser(
        id = id,
        username = username,
        name = name,
        bio = bio,
        birthDate = birthDate,
        avatarUrl = avatarUrl
    )
}

fun LocalUser.toUser() : User {
    return User(
        id = id,
        username = username,
        name = name,
        bio = bio,
        birthDate = birthDate,
        avatarUrl = avatarUrl,
        status = Status.ONLINE,
        lastSeen = LocalDateTime.now()
    )
}