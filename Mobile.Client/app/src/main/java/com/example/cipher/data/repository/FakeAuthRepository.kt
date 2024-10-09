package com.example.cipher.data.repository

import com.example.cipher.domain.models.auth.AuthResult
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest
import com.example.cipher.domain.repository.auth.AuthRepository

class FakeAuthRepository : AuthRepository {
    private var shouldReturnError = false

    override suspend fun signUp(request: SignUpRequest): AuthResult<Unit> {
        shouldReturnError = false
        return if (shouldReturnError) {
            AuthResult.UnknownError()
        } else {
            AuthResult.Authorized(Unit)
        }
    }

    override suspend fun signIn(request: SignInRequest): AuthResult<Unit> {
        shouldReturnError = false
        return if (shouldReturnError) {
            AuthResult.UnknownError()
        } else {
            AuthResult.Authorized(Unit)
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        shouldReturnError = true
        return if (shouldReturnError) {
            AuthResult.UnknownError()
        } else {
            AuthResult.Authorized(Unit)
        }
    }
}
