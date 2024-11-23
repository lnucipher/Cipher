package com.example.cipher.domain.models.user

sealed class EditResult {
    data object Success: EditResult()
    data class Error(val errorMessage: String) : EditResult()
}