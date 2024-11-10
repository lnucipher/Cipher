package com.example.cipher.data.remote.api.dto

import com.example.cipher.domain.models.message.Message

data class PagerMessageResponseDto(
    val pageNumber: Int,
    val pageSize: Int,
    val totalCount: Int,
    val totalPages: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean,
    val items: List<Message>
)