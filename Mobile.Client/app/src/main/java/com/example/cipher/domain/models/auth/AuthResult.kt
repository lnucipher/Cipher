package com.example.cipher.domain.models.auth

sealed class AuthResult<out T> {
    data object Authorized: AuthResult<Nothing>()
    data class Error(val errorMessage: String) : AuthResult<Nothing>()
    data class WithData<T>(val data: T) : AuthResult<Boolean>()
}