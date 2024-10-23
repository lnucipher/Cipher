package com.example.cipher.domain.repository.user

import com.example.cipher.domain.models.user.LocalUser

interface LocalUserManager {
    suspend fun saveUser(user: LocalUser)
    suspend fun getUser(): LocalUser
    suspend fun clearUser()
}