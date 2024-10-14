package com.example.cipher.data.network.api.dto

import com.example.cipher.domain.models.user.LocalUser

data class AuthResponseDto (
    val user: LocalUser,
    val token: String
)