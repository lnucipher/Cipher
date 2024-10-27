package com.example.cipher.data.remote.api.dto

data class AuthResponseDto (
    val id: String,
    val username: String,
    val name: String,
    val bio: String,
    val birthDate: String,
    val avatarUrl: String,
    val token: String
)