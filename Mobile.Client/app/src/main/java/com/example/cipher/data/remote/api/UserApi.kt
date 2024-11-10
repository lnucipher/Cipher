package com.example.cipher.data.remote.api

import com.example.cipher.data.remote.api.dto.SearchUserDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {
    @GET("api/users/search")
    suspend fun searchUsers(
        @Query("requestorId") requestorId: String,
        @Query("searchedUsername") searchedUsername: String
    ): ApiResponse<List<SearchUserDto>>
}