package com.example.cipher.domain.repository.auth

interface JwtTokenManager {
    suspend fun saveAccessJwt(token: String)
    suspend fun getAccessJwt(): String?
    suspend fun clearAllTokens()
}