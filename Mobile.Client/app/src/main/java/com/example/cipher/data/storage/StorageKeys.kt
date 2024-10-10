package com.example.cipher.data.storage

import androidx.datastore.preferences.core.stringPreferencesKey

internal object StorageJwtKeys {
    val ACCESS_JWT_KEY = stringPreferencesKey("access_jwt")
}
