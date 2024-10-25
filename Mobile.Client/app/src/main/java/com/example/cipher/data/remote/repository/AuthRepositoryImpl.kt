package com.example.cipher.data.remote.repository

import com.example.cipher.data.mappers.toLocalUser
import com.example.cipher.data.remote.api.AuthApi
import com.example.cipher.domain.models.auth.AuthResult
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest
import com.example.cipher.domain.repository.auth.AuthRepository
import com.example.cipher.domain.repository.auth.JwtTokenManager
import com.example.cipher.domain.repository.user.LocalUserManager
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.statusCode
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: JwtTokenManager,
    private val localUserManager: LocalUserManager
) : AuthRepository {

    override suspend fun signUp(request: SignUpRequest, avatarUrl: String?): AuthResult {
        return when (val response = api.signUp(request, convertImgUrlToMultipart(avatarUrl))) {
            is ApiResponse.Success -> {
                tokenManager.saveAccessJwt(response.data.token)
                localUserManager.saveUser(response.data.toLocalUser())

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

    override suspend fun signIn(request: SignInRequest): AuthResult {
        return when (val response = api.signIn(request)) {
            is ApiResponse.Success -> {
                tokenManager.saveAccessJwt(response.data.token)
                localUserManager.saveUser(response.data.toLocalUser())

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

    override suspend fun ifUserExist(username: String): Boolean? {
        return when (val response = api.isUserExist(username)) {
            is ApiResponse.Success -> response.data.value
            is ApiResponse.Failure.Exception -> {
                null
            }
            is ApiResponse.Failure.Error -> {
                handleAuthError(response.statusCode.code)
                null
            }
        }
    }

    override suspend fun logout() {
        tokenManager.clearAllTokens()
        localUserManager.clearUser()
    }

    private fun handleAuthError(statusCode: Int): AuthResult {
        return when (statusCode) {
            401 -> AuthResult.Unauthorized
            400 -> AuthResult.BadRequest
            else -> AuthResult.UnknownError
        }
    }

    private fun convertImgUrlToMultipart(url: String?): MultipartBody.Part?  {
        if (url == null) return null

        val file = File(url)
        if (!file.exists()) return null

        return MultipartBody.Part
            .createFormData(
                "avatarFile",
                "avatarFile.${file.extension.lowercase()}",
                file.asRequestBody()
            )
    }
}

