package com.example.cipher.data.network.api

import com.example.cipher.data.network.api.dto.ApiResponseWrapper
import com.example.cipher.data.network.api.dto.AuthResponseDto
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {

    @Multipart
    @POST("auth/signUp")
    suspend fun signUp(
        @Part("requestBody") request: SignUpRequest,
        @Part avatarFile: MultipartBody.Part?
    ): ApiResponse<ApiResponseWrapper<AuthResponseDto>>

    @POST("auth/signIn")
    suspend fun signIn(
        @Body request: SignInRequest
    ): ApiResponse<ApiResponseWrapper<AuthResponseDto>>

    @GET("auth/checkIfExists")
    suspend fun checkIfExists(
        @Part username: String
    ): ApiResponse<ApiResponseWrapper<Boolean>>
}