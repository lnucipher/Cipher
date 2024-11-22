package com.example.cipher.data.remote.api

import com.example.cipher.data.remote.api.dto.SearchUserDto
import com.example.cipher.data.remote.api.dto.update.UpdateAvatarRequestDto
import com.example.cipher.data.remote.api.dto.update.UpdateAvatarResponseDto
import com.example.cipher.data.remote.api.dto.update.UpdatePasswordRequestDto
import com.example.cipher.data.remote.api.dto.update.UpdateProfileRequestDto
import com.example.cipher.data.remote.api.dto.update.UpdateProfileResponseDto
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserApi {
    @GET("api/users/search")
    suspend fun searchUsers(
        @Query("requestorId") requestorId: String,
        @Query("searchedUsername") searchedUsername: String
    ): ApiResponse<List<SearchUserDto>>

    @PATCH("api/users/password")
    suspend fun updatePassword(
        @Body request: UpdatePasswordRequestDto
    ): Response<Unit>

    @Multipart
    @POST("api/users/avatar")
    suspend fun updateAvatar(
        @Part("requestBody") request: UpdateAvatarRequestDto,
        @Part avatarFile: MultipartBody.Part?
    ): Response<UpdateAvatarResponseDto>

    @PATCH("api/users")
    suspend fun updateUserProfile(
        @Body request: UpdateProfileRequestDto
    ): Response<UpdateProfileResponseDto>
}