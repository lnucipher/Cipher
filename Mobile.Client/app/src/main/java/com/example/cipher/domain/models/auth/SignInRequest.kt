package com.example.cipher.domain.models.auth

data class SignInRequest (
    val username: String,
    val password: String
)