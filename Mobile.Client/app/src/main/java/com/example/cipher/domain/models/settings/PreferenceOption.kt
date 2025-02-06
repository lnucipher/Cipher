package com.example.cipher.domain.models.settings

import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.ui.graphics.Color

sealed interface PreferenceOption {
    val label: String
}

enum class NotificationSound(override val label: String, val uri: Uri?) : PreferenceOption {
    DEFAULT("Default", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)),
    ALARM("Alarm", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)),
    RINGTONE("Ringtone", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)),
    NONE("None", null)
}

enum class NotificationVibration(override val label: String, val vibration: LongArray) : PreferenceOption {
    NONE("None", longArrayOf(0)),
    SHORT("Short", longArrayOf(0, 200, 100, 200)),
    LONG("Long", longArrayOf(0, 700, 250, 700)),
    DEFAULT("Default", longArrayOf(0, 500, 250, 500))
}

enum class Language(override val label: String) : PreferenceOption {
    ENGLISH("English"),
    UKRAINE("Ukraine"),
}

enum class Theme(
    override val label: String,
    val lightPrimaryBackground: Color,
    val lightPrimaryText: Color,
    val lightSecondaryBackground: Color,
    val lightTintColor: Color,
    val darkPrimaryBackground: Color,
    val darkPrimaryText: Color,
    val darkSecondaryBackground: Color,
    val darkTintColor: Color
) : PreferenceOption {
    DEFAULT(
        "default",
        lightPrimaryBackground = Color(0xFFFFFFFF),
        lightPrimaryText = Color(0xFF000000),
        lightSecondaryBackground = Color(0xFFF8FAFF),
        lightTintColor = Color(0xFFAFBBF7),
        darkPrimaryBackground = Color(0xFF1E1E1E),
        darkPrimaryText = Color(0xFFFFFFFF),
        darkSecondaryBackground = Color(0xFF141414),
        darkTintColor = Color(0xFF3045C5),
    )
}