package com.example.cipher.data.remote.api.dto

import com.example.cipher.domain.models.user.LocalUser

data class AuthResponseDto (
    val user: LocalUser,
    val token: String
)