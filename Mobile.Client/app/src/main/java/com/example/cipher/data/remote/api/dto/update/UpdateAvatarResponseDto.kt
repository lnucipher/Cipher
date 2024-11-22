package com.example.cipher.data.remote.api.dto.update

data class UpdateAvatarResponseDto (
    val id: String,
    val oldAvatarUrl: String,
    val newAvatarUrl: String
)