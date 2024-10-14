package com.example.cipher.data.repository

import com.example.cipher.domain.models.auth.AuthResult
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest
import com.example.cipher.domain.repository.auth.AuthRepository

class FakeAuthRepository : AuthRepository {
    private var shouldReturnError = false

    override suspend fun signUp(request: SignUpRequest, avatarUrl: String?): AuthResult {
        shouldReturnError = false
        return if (shouldReturnError) {
            AuthResult.UnknownError
        } else {
            AuthResult.Authorized
        }
    }

    override suspend fun signIn(request: SignInRequest): AuthResult {
        return AuthResult.BadRequest
    }

    override suspend fun checkIdUserExist(username: String): Boolean {
        return true
    }

    override suspend fun logout() {
    }
}
