package com.example.cipher.data.local.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.cipher.data.PreferencesKeys.DARK_THEME_KEY
import com.example.cipher.data.PreferencesKeys.IS_NOTIFICATION_ENABLED_KEY
import com.example.cipher.data.PreferencesKeys.LANGUAGE_KEY
import com.example.cipher.data.PreferencesKeys.MESSAGE_CORNERS_SIZE_KEY
import com.example.cipher.data.PreferencesKeys.MESSAGE_FONT_SIZE_KEY
import com.example.cipher.data.PreferencesKeys.NOTIFICATION_SOUND_KEY
import com.example.cipher.data.PreferencesKeys.NOTIFICATION_VIBRATION_KEY
import com.example.cipher.data.PreferencesKeys.THEME_KEY
import com.example.cipher.domain.models.settings.Language
import com.example.cipher.domain.models.settings.NotificationSound
import com.example.cipher.domain.models.settings.NotificationVibration
import com.example.cipher.domain.models.settings.Settings
import com.example.cipher.domain.models.settings.Theme
import com.example.cipher.domain.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override suspend fun getNotificationSound(): NotificationSound {
        val preferences = dataStore.data.first()
        val sound = preferences[NOTIFICATION_SOUND_KEY] ?: NotificationSound.DEFAULT.name
        return NotificationSound.valueOf(sound)
    }

    override suspend fun setNotificationSound(notificationSound: NotificationSound) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_SOUND_KEY] = notificationSound.name
        }
    }

    override suspend fun getNotificationVibration(): NotificationVibration {
        val preferences = dataStore.data.first()
        val vibration = preferences[NOTIFICATION_VIBRATION_KEY] ?: NotificationVibration.DEFAULT.name
        return NotificationVibration.valueOf(vibration)
    }

    override suspend fun setNotificationVibration(notificationVibration: NotificationVibration) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_VIBRATION_KEY] = notificationVibration.name
        }
    }

    override suspend fun getLanguage(): Language {
        val preferences = dataStore.data.first()
        val language = preferences[LANGUAGE_KEY] ?: Language.ENGLISH.name
        return Language.valueOf(language)
    }

    override suspend fun setLanguage(language: Language) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language.name
        }
    }

    override suspend fun getMessageCornersSize(): Int {
        val preferences = dataStore.data.first()
        return preferences[MESSAGE_CORNERS_SIZE_KEY] ?: 12
    }

    override suspend fun setMessageCornersSize(size: Int) {
        dataStore.edit { preferences ->
            preferences[MESSAGE_CORNERS_SIZE_KEY] = size
        }
    }

    override suspend fun getMessageFontSize(): Int {
        val preferences = dataStore.data.first()
        return preferences[MESSAGE_FONT_SIZE_KEY] ?: 12
    }

    override suspend fun setMessageFontSize(size: Int) {
        dataStore.edit { preferences ->
            preferences[MESSAGE_FONT_SIZE_KEY] = size
        }
    }

    override suspend fun isDarkThemeEnabled(): Boolean? {
        val preferences = dataStore.data.first()
        return preferences[DARK_THEME_KEY]
    }

    override suspend fun setDarkThemeEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isEnabled
        }
    }

    override suspend fun getTheme(): Theme {
        val preferences = dataStore.data.first()
        val theme = preferences[THEME_KEY] ?: Theme.DEFAULT.name
        return Theme.valueOf(theme)
    }

    override suspend fun setTheme(theme: Theme) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    override suspend fun isNotificationEnabled(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[IS_NOTIFICATION_ENABLED_KEY] ?: true
    }

    override suspend fun setNotificationEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_NOTIFICATION_ENABLED_KEY] = isEnabled
        }
    }

    override suspend fun getSettings(): Settings {
        val preferences = dataStore.data.first()
        return Settings(
            notificationSound = NotificationSound.valueOf(preferences[NOTIFICATION_SOUND_KEY] ?: NotificationSound.DEFAULT.name),
            notificationVibration = NotificationVibration.valueOf(preferences[NOTIFICATION_VIBRATION_KEY] ?: NotificationVibration.DEFAULT.name),
            language = Language.valueOf(preferences[LANGUAGE_KEY] ?: Language.ENGLISH.name),
            theme = Theme.valueOf(preferences[THEME_KEY] ?: Theme.DEFAULT.name),
            messageCornersSize = preferences[MESSAGE_CORNERS_SIZE_KEY] ?: 12,
            messageFontSize = preferences[MESSAGE_FONT_SIZE_KEY] ?: 16,
            darkTheme = preferences[DARK_THEME_KEY],
            isNotificationEnabled = preferences[IS_NOTIFICATION_ENABLED_KEY] ?: true
        )
    }

    override suspend fun getSettingsFlow(): Flow<Settings> {
        return dataStore.data.map { preferences ->
            Settings(
                notificationSound = NotificationSound.valueOf(preferences[NOTIFICATION_SOUND_KEY] ?: NotificationSound.DEFAULT.name),
                notificationVibration = NotificationVibration.valueOf(preferences[NOTIFICATION_VIBRATION_KEY] ?: NotificationVibration.DEFAULT.name),
                language = Language.valueOf(preferences[LANGUAGE_KEY] ?: Language.ENGLISH.name),
                theme = Theme.valueOf(preferences[THEME_KEY] ?: Theme.DEFAULT.name),
                messageCornersSize = preferences[MESSAGE_CORNERS_SIZE_KEY] ?: 12,
                messageFontSize = preferences[MESSAGE_FONT_SIZE_KEY] ?: 16,
                darkTheme = preferences[DARK_THEME_KEY],
                isNotificationEnabled = preferences[IS_NOTIFICATION_ENABLED_KEY] ?: true
            )
        }
    }
}
