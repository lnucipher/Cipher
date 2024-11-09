package com.example.cipher.data.remote.api.dto

import com.example.cipher.domain.models.user.User

data class SearchUserDto (
    val isContact: Boolean,
    val user: User
)
