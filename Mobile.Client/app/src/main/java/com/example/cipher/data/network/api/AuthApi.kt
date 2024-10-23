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
import retrofit2.http.Query

interface AuthApi {

    @Multipart
    @POST("api/auth/signUp")
    suspend fun signUp(
        @Part("requestBody") request: SignUpRequest,
        @Part avatarFile: MultipartBody.Part?
    ): ApiResponse<ApiResponseWrapper<AuthResponseDto>>

    @POST("api/auth/signIn")
    suspend fun signIn(
        @Body request: SignInRequest
    ): ApiResponse<ApiResponseWrapper<AuthResponseDto>>

    @GET("api/auth/isUserExist")
    suspend fun isUserExist(
        @Query("username") username: String
    ): ApiResponse<ApiResponseWrapper<Boolean>>
}