package com.example.cipher.domain.repository.user

import com.example.cipher.data.remote.api.dto.update.UpdatePasswordRequestDto
import com.example.cipher.data.remote.api.dto.update.UpdateProfileRequestDto
import com.example.cipher.domain.models.user.EditResult
import com.example.cipher.domain.models.user.User

interface UserRepository {
    suspend fun searchUsers(requestorId: String, searchedUsername: String): List<Pair<User, Boolean>>
    suspend fun updatePassword(request: UpdatePasswordRequestDto): EditResult
    suspend fun updateAvatar(userId: String, avatarUrl: String?): EditResult
    suspend fun updateProfile(request: UpdateProfileRequestDto): EditResult
}