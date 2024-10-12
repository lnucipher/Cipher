package com.example.cipher.data.network.api.dto

import com.example.cipher.domain.models.user.User

data class AuthResponseDto (
    val user: User,
    val token: String
)