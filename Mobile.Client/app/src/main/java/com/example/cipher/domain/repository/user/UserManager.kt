package com.example.cipher.domain.repository.user

import com.example.cipher.domain.models.user.User

interface UserManager {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User
    suspend fun clearUser()
}