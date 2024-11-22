package com.example.cipher.data.remote.api.dto.update

data class UpdatePasswordRequestDto (
    val id: String,
    val currentPassword: String,
    val newPassword: String
)