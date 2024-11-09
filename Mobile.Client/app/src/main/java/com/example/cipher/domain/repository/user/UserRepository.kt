package com.example.cipher.domain.repository.user

import com.example.cipher.domain.models.user.User

interface UserRepository {
    suspend fun searchUsers(requestorId: String, searchedUsername: String): List<User>
}