package com.example.cipher.domain.models.auth


data class SignUpRequest (
    val username: String,
    val password: String,
    val name: String,
    val bio: String?,
    val birthDate: String?,
)
