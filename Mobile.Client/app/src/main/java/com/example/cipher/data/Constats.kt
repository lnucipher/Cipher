package com.example.cipher.data

import androidx.datastore.preferences.core.stringPreferencesKey

internal object PreferencesKeys {
    val ACCESS_JWT_KEY = stringPreferencesKey("access_jwt")
}

internal object NetworkKeys {
    const val BASE_URL = ""

    const val HEADER_AUTHORIZATION = "Authorization"
    const val TOKEN_TYPE = "Bearer"
}

internal object StorageKeys {
    const val JWT_TOKEN_PREFERENCES = "jwt_token_preferences"

    const val LOCAL_USER_PROTO_FILE = "local_user.proto"
}
