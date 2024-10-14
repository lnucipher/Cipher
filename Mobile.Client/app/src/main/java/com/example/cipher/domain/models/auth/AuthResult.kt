package com.example.cipher.domain.models.auth

sealed class AuthResult {
    data object Authorized: AuthResult()
    data object Unauthorized: AuthResult()
    data object BadRequest: AuthResult()
    data object UnknownError: AuthResult()
}