package com.example.cipher.data.remote.api.dto

import com.example.cipher.domain.models.user.User

data class PagerContactResponseDto (
    val pageNumber: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean,
    val items: List<User>
)