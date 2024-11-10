package com.example.cipher.data

import androidx.datastore.preferences.core.stringPreferencesKey

internal object PreferencesKeys {
    val ACCESS_JWT_KEY = stringPreferencesKey("access_jwt")
}

internal object NetworkKeys {
    const val IDENTITY_SERVER_BASE_URL = "http://212.23.203.37:4000/"
    const val CHAT_SERVER_BASE_URL = "http://10.0.2.2:3000/"
    const val CHAT_SERVER_HUB_URL = "ws://10.0.2.2:3000/api/chat-hub"

    const val HEADER_AUTHORIZATION = "Authorization"
    const val TOKEN_TYPE = "Bearer"
}

internal object StorageKeys {
    const val JWT_TOKEN_PREFERENCES = "jwt_token_preferences"

    const val LOCAL_USER_PROTO_FILE = "local_user.proto"
}
