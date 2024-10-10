package com.example.cipher.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.cipher.domain.repository.auth.JwtTokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JwtTokenStorage @Inject constructor(
    private val jwtDataStore: DataStore<Preferences>
) : JwtTokenManager {

    override suspend fun saveAccessJwt(token: String) {
        jwtDataStore.edit { preferences ->
            preferences[StorageJwtKeys.ACCESS_JWT_KEY] = token
        }
    }

    override suspend fun getAccessJwt(): String? {
        return jwtDataStore.data.map { preferences ->
            preferences[StorageJwtKeys.ACCESS_JWT_KEY]
        }.first()
    }

    override suspend fun clearAllTokens() {
        jwtDataStore.edit { preferences ->
            preferences.remove(StorageJwtKeys.ACCESS_JWT_KEY)
        }
    }
}