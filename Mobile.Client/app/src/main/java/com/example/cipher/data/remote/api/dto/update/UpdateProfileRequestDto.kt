package com.example.cipher.data.remote.api.dto.update

data class UpdateProfileRequestDto(
    val id: String,
    val username: String?,
    val name: String?,
    val bio: String?,
    val birthDate: String?
)
