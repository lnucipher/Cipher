package com.example.cipher.data.network.api.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponseWrapper<T>(
    val value: T,
    val errorMessage: String?
)