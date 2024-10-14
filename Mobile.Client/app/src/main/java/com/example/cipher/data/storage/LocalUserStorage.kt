package com.example.cipher.data.storage

import androidx.datastore.core.DataStore
import com.example.cipher.LocalUserProto
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.repository.user.LocalUserManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LocalUserStorage @Inject constructor(
    private val dataStore: DataStore<LocalUserProto>
): LocalUserManager {
    override suspend fun saveUser(user: LocalUser) {
        dataStore.updateData {
            it.toBuilder()
                .setId(user.id)
                .setUsername(user.username)
                .setName(user.name)
                .setBio(user.bio)
                .setBirthDate(user.birthDate)
                .setAvatarUrl(user.avatarUrl)
                .build()
        }
    }

    override suspend fun getUser(): LocalUser {
        return dataStore.data.first().let {
            LocalUser(
                id = it.id,
                username = it.username,
                name = it.name,
                bio = it.bio,
                birthDate = it.birthDate,
                avatarUrl = it.avatarUrl
            )
        }
    }

    override suspend fun clearUser() {
        dataStore.updateData { userProto ->
            userProto.toBuilder()
                .clearId()
                .clearUsername()
                .clearName()
                .clearBio()
                .clearBirthDate()
                .clearAvatarUrl()
                .build()
        }
    }
}