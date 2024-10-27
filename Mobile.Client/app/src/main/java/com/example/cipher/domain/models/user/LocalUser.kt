package com.example.cipher.domain.models.user

import kotlinx.serialization.Serializable

@Serializable
data class LocalUser (
    val id: String,
    val username: String,
    val name: String,
    val bio: String,
    val birthDate: String,
    val avatarUrl: String
)