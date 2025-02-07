package com.example.cipher.domain.models.settings

data class Settings(
    val isNotificationEnabled: Boolean,
    val notificationSound: NotificationSound,
    val notificationVibration: NotificationVibration,
    val language: Language,
    val messageCornersSize: Int,
    val messageFontSize: Int,
    val darkTheme: Boolean?,
    val theme: Theme
) {
    companion object {
        fun getDefaultSettings() = Settings(
            isNotificationEnabled = true,
            notificationSound = NotificationSound.DEFAULT,
            notificationVibration = NotificationVibration.DEFAULT,
            language = Language.ENGLISH,
            messageCornersSize = 12,
            messageFontSize = 16,
            darkTheme = true,
            theme = Theme.DEFAULT
        )
    }
}