package com.example.cipher.data.remote.api

import com.example.cipher.data.remote.api.dto.PagerMessageResponseDto
import com.example.cipher.domain.models.message.MessageRequest
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageApi {

    @POST("api/messages")
    suspend fun addMessage(
        @Body request: MessageRequest
    ): ApiResponse<Unit>

    @GET("api/messages")
    suspend fun getMessages(
        @Query("senderId") senderId: String,
        @Query("receiverId") receiverId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): PagerMessageResponseDto
}