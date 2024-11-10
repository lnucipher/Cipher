package com.example.cipher.data.remote.api

import com.example.cipher.data.remote.api.dto.PagerContactResponseDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ContactApi {
    @GET("api/contacts")
    suspend fun getContacts(
        @Query("requestorId") userId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): ApiResponse<PagerContactResponseDto>
}