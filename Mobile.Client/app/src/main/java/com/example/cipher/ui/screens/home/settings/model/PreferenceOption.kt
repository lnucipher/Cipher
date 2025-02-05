package com.example.cipher.ui.screens.home.settings.model

sealed interface PreferenceOption {
    val label: String
}

enum class NotificationSound(override val label: String) : PreferenceOption {
    DEFAULT("Default"),
    BEEP("Beep"),
    CHIME("Chime"),
    ALERT("Alert"),
    NONE("NONE")
}

enum class NotificationVibration(override val label: String) : PreferenceOption {
    NONE("None"),
    SHORT("Short"),
    LONG("Long"),
    DEFAULT("Default")
}

enum class Language(override val label: String) : PreferenceOption {
    ENGLISH("English"),
    UKRAINE("Ukraine"),
}

enum class DialogType {
    SOUND, VIBRATION, LANGUAGE
}