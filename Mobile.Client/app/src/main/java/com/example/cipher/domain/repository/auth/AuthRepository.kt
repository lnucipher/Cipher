package com.example.cipher.domain.repository.auth

import android.content.Context
import com.example.cipher.domain.models.auth.AuthResult
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest

interface AuthRepository {
    suspend fun signUp(request: SignUpRequest, avatarUrl: String?): AuthResult<Nothing>
    suspend fun signIn(request: SignInRequest): AuthResult<Nothing>
    suspend fun ifUserExist(username: String): AuthResult<Boolean>
    suspend fun logout(context: Context)
}