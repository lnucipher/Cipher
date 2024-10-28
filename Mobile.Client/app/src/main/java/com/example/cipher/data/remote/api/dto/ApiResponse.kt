package com.example.cipher.data.remote.api.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponseWrapper<T>(
    val value: T,
    val isSuccess: Boolean,
    val errorMessage: String?
)