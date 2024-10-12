package com.example.cipher.domain.repository.auth

import com.example.cipher.domain.models.auth.AuthResult
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest

interface AuthRepository {
    suspend fun signUp(request: SignUpRequest): AuthResult
    suspend fun signIn(request: SignInRequest): AuthResult
}