package com.example.cipher.data.repository

import com.example.cipher.data.network.api.AuthApi
import com.example.cipher.domain.models.auth.AuthResult
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest
import com.example.cipher.domain.repository.auth.AuthRepository
import com.example.cipher.domain.repository.auth.JwtTokenManager
import com.example.cipher.domain.repository.user.UserManager
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.statusCode

class AuthRepositoryImpl constructor(
    private val api: AuthApi,
    private val tokenManager: JwtTokenManager,
    private val userManager: UserManager
) : AuthRepository {

    override suspend fun signUp(request: SignUpRequest): AuthResult {
        return when (val response = api.signUp(request)) {
            is ApiResponse.Success -> {
                signIn(
                    request = SignInRequest(
                        request.username,
                        request.password
                    )
                )
            }
            is ApiResponse.Failure.Error -> {
                handleAuthError(response.statusCode.code)
            }
            is ApiResponse.Failure.Exception -> {
                AuthResult.UnknownError
            }
        }
    }

    override suspend fun signIn(request: SignInRequest): AuthResult {
        return when (val response = api.signIn(request)) {
            is ApiResponse.Success -> {
                tokenManager.saveAccessJwt(response.data.token)
                userManager.saveUser(response.data.user)

                AuthResult.Authorized
            }
            is ApiResponse.Failure.Error -> {
                handleAuthError(response.statusCode.code)
            }
            is ApiResponse.Failure.Exception -> {
                AuthResult.UnknownError
            }
        }
    }

    private fun handleAuthError(statusCode: Int): AuthResult {
        return when (statusCode) {
            401 -> AuthResult.Unauthorized
            else -> AuthResult.UnknownError
        }
    }
}

