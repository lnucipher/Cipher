package com.example.cipher.domain.repository.settings

import com.example.cipher.domain.models.settings.Language
import com.example.cipher.domain.models.settings.NotificationSound
import com.example.cipher.domain.models.settings.NotificationVibration
import com.example.cipher.domain.models.settings.Settings
import com.example.cipher.domain.models.settings.Theme

interface SettingsRepository {
    suspend fun getNotificationSound(): NotificationSound
    suspend fun setNotificationSound(notificationSound: NotificationSound)

    suspend fun getNotificationVibration(): NotificationVibration
    suspend fun setNotificationVibration(notificationVibration: NotificationVibration)

    suspend fun getLanguage(): Language
    suspend fun setLanguage(language: Language)

    suspend fun getMessageCornersSize(): Int
    suspend fun setMessageCornersSize(size: Int)

    suspend fun getMessageFontSize(): Int
    suspend fun setMessageFontSize(size: Int)

    suspend fun isDarkThemeEnabled(): Boolean?
    suspend fun setDarkThemeEnabled(isEnabled: Boolean)

    suspend fun getTheme(): Theme
    suspend fun setTheme(theme: Theme)

    suspend fun isNotificationEnabled(): Boolean
    suspend fun setNotificationEnabled(isEnabled: Boolean)

    suspend fun getSettings(): Settings
}