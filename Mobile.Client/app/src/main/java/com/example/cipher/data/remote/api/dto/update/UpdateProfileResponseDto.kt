package com.example.cipher.data.remote.api.dto.update

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProfileResponseDto(
    val id: String,
    val username: String,
    val name: String,
    val bio: String,
    val birthDate: String,
    val avatarUrl: String
)
