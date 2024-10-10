package com.example.cipher.data

import androidx.datastore.preferences.core.stringPreferencesKey

internal object PreferencesKeys {
    const val JWT_TOKEN_PREFERENCES = "jwt_token_preferences"
    const val USER_INFO_PREFERENCES = "user_info_preferences"
}

internal object NetworkKeys {
    const val BASE_URL = ""

    const val HEADER_AUTHORIZATION = "Authorization"
    const val TOKEN_TYPE = "Bearer"
}

internal object StorageKeys {
    val ACCESS_JWT_KEY = stringPreferencesKey("access_jwt")
}
