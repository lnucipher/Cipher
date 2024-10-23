package com.example.cipher.data.local.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.cipher.data.PreferencesKeys.ACCESS_JWT_KEY
import com.example.cipher.domain.repository.auth.JwtTokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JwtTokenStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : JwtTokenManager {

    override suspend fun saveAccessJwt(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_JWT_KEY] = token
        }
    }

    override suspend fun getAccessJwt(): String? {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_JWT_KEY]
        }.first()
    }

    override suspend fun clearAllTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_JWT_KEY)
        }
    }
}