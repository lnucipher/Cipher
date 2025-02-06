package com.example.cipher.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal object PreferencesKeys {
    val ACCESS_JWT_KEY = stringPreferencesKey("access_jwt")

    val NOTIFICATION_SOUND_KEY = stringPreferencesKey("notification_sound")
    val NOTIFICATION_VIBRATION_KEY = stringPreferencesKey("notification_vibration")
    val LANGUAGE_KEY = stringPreferencesKey("language")
    val MESSAGE_CORNERS_SIZE_KEY = intPreferencesKey("message_corners_size")
    val MESSAGE_FONT_SIZE_KEY = intPreferencesKey("message_font_size")
    val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    val THEME_KEY = stringPreferencesKey("theme")
    val IS_NOTIFICATION_ENABLED_KEY = booleanPreferencesKey("is_notification_enabled")

}

internal object NetworkKeys {
    private const val BASE_URL = "http://10.0.2.2:5000/"
    const val IDENTITY_SERVER_BASE_URL = BASE_URL + "identity/"
    const val CHAT_SERVER_BASE_URL = BASE_URL + "chat/"
    const val CHAT_SERVER_HUB_URL = "ws://10.0.2.2:5000/chat/api/chat-hub"

    const val HEADER_AUTHORIZATION = "Authorization"
    const val TOKEN_TYPE = "Bearer"
}

internal object StorageKeys {
    const val JWT_TOKEN_PREFERENCES = "jwt_token_preferences"
    const val LOCAL_USER_PROTO_FILE = "local_user.proto"
    const val SETTINGS_PREFERENCES = "settings_preference"
}
