package com.example.cipher.data.network.api

import com.example.cipher.data.network.api.dto.AuthResponseDto
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/signUp")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): ApiResponse<Unit>

    @POST("auth/signIn")
    suspend fun signIn(
        @Body request: SignInRequest
    ): ApiResponse<AuthResponseDto>

}