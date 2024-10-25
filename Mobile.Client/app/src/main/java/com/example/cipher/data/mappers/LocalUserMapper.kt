package com.example.cipher.data.mappers

import com.example.cipher.data.remote.api.dto.AuthResponseDto
import com.example.cipher.domain.models.user.LocalUser

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