package com.example.cipher.domain.models.auth

import java.time.LocalDate

data class SignUpRequest (
    val username: String,
    val password: String,
    val name: String,
    val bio: String,
    val birthDate: LocalDate,
    val avatarUrl: String
)
