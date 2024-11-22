package com.example.cipher.data.remote.repository

import android.content.Context
import com.example.cipher.data.mappers.ImageMapper.convertImgUrlToMultipart
import com.example.cipher.data.remote.api.UserApi
import com.example.cipher.data.remote.api.dto.ErrorResponse
import com.example.cipher.data.remote.api.dto.update.UpdateAvatarRequestDto
import com.example.cipher.data.remote.api.dto.update.UpdatePasswordRequestDto
import com.example.cipher.data.remote.api.dto.update.UpdateProfileRequestDto
import com.example.cipher.domain.models.user.EditResult
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.user.LocalUserManager
import com.example.cipher.domain.repository.user.UserRepository
import com.skydoves.sandwich.ApiResponse
import com.squareup.moshi.Moshi
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val localUserManager: LocalUserManager,
    private val context: Context,
    moshi: Moshi
): UserRepository {

    private val jsonAdapter = moshi.adapter(ErrorResponse::class.java)

    override suspend fun searchUsers(requestorId: String, searchedUsername: String): List<Pair<User, Boolean>> {
        return when (
            val response = api.searchUsers(
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

    override suspend fun updatePassword(request: UpdatePasswordRequestDto): EditResult {
        return try {
            val response = api.updatePassword(request)

            if (response.isSuccessful) {
                EditResult.Success
            } else {
                val errorBody = response.errorBody()
                val errorResponse = errorBody?.let { jsonAdapter.fromJson(it.string()) }

                EditResult.Error(errorResponse?.error ?: "Unknown error")
            }
        } catch (exception: IOException) {
            EditResult.Error("Connection failed")
        } catch (exception: Exception) {
            EditResult.Error("An error occurred: ${exception.localizedMessage}")
        }
    }

    override suspend fun updateAvatar(userId: String, avatarUrl: String?): EditResult {
        return try {
            val response = api.updateAvatar(
                UpdateAvatarRequestDto(userId),
                convertImgUrlToMultipart(context, avatarUrl)
            )

            if (response.isSuccessful) {
                val responseBody = response.body()

                responseBody?.let {
                    val localUser = localUserManager.getUser()
                    localUserManager.saveUser(localUser.copy(avatarUrl = responseBody.newAvatarUrl))
                }

                EditResult.Success
            } else {
                val errorBody = response.errorBody()
                val errorResponse = errorBody?.let { jsonAdapter.fromJson(it.string()) }

                EditResult.Error(errorResponse?.error ?: "Unknown error")
            }
        } catch (exception: IOException) {
            EditResult.Error("Connection failed")
        } catch (exception: Exception) {
            EditResult.Error("An error occurred: ${exception.localizedMessage}")
        }
    }

    override suspend fun updateProfile(request: UpdateProfileRequestDto):EditResult {
        return try {
            val response = api.updateUserProfile(request)

            if (response.isSuccessful) {
                val responseBody = response.body()

                responseBody?.let {
                    val localUser = localUserManager.getUser()
                    localUserManager.saveUser(localUser.copy(
                        username = responseBody.username,
                        name = responseBody.name,
                        bio = responseBody.bio,
                        birthDate = responseBody.birthDate
                    ))
                }

                EditResult.Success
            } else {
                val errorBody = response.errorBody()
                val errorResponse = errorBody?.let { jsonAdapter.fromJson(it.string()) }

                EditResult.Error(errorResponse?.error ?: "Unknown error")
            }
        } catch (exception: IOException) {
            EditResult.Error("Connection failed")
        } catch (exception: Exception) {
            EditResult.Error("An error occurred: ${exception.localizedMessage}")
        }
    }

}