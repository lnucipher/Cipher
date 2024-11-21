package com.example.cipher.data.remote.api

import com.example.cipher.data.remote.api.dto.AddContactRequestDto
import com.example.cipher.data.remote.api.dto.PagerContactResponseDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ContactApi {
    @GET("api/contacts")
    suspend fun getContacts(
        @Query("requestorId") userId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): ApiResponse<PagerContactResponseDto>

    @POST("api/contacts")
    suspend fun addContact(
        @Body request: AddContactRequestDto
    ): ApiResponse<Unit>

    @DELETE("api/contacts")
    suspend fun deleteContact(
        @Query("primaryUserId") primaryUserId: String,
        @Query("secondaryUserId") secondaryUserId: String
    ): ApiResponse<Unit>
}