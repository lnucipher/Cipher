package com.example.cipher.data.remote.repository

import com.example.cipher.data.remote.api.UserApi
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.user.UserRepository
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
): UserRepository {
    override suspend fun searchUsers(requestorId: String, searchedUsername: String): List<Pair<User, Boolean>> {
        return when (
            val response = userApi.searchUsers(
                requestorId = requestorId,
                searchedUsername = searchedUsername
            )
        ) {
            is ApiResponse.Success -> response.data
                .sortedByDescending { it.isContact }
                .map { Pair(it.user, it.isContact) }
            is ApiResponse.Failure -> emptyList()
        }
    }
}