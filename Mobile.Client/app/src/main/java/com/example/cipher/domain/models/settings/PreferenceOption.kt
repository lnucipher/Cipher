package com.example.cipher.domain.models.settings

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.ui.graphics.Color
import com.example.cipher.R

sealed interface PreferenceOption {
    val label: String
}

enum class NotificationSound(override val label: String, private val rawResId: Int?) : PreferenceOption {
    DEFAULT("Default", null),
    BELL("Bell", R.raw.bell),
    MARIMBA("Marimba", R.raw.marimba),
    PLUCK("Pluck", R.raw.pluck),
    POP("Pop", R.raw.pop),
    TRAIN("Train", R.raw.train),
    STEAM("Steam", R.raw.steam),
    NONE("None", null);

    fun getUri(context: Context): Uri? {
        return when (this) {
            DEFAULT -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            NONE -> null
            else -> Uri.parse("android.resource://${context.packageName}/${rawResId}")
        }
    }
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
    val lightPalette: ThemePalette,
    val darkPalette: ThemePalette
) : PreferenceOption {
    DEFAULT(
        label = "Default",
        lightPalette = ThemePalette(
            primaryBackground = Color(0xFFFFFFFF),
            primaryText = Color(0xFF000000),
            secondaryBackground = Color(0xFFF8FAFF),
            secondaryText = Color(0xFF777777),
            tertiaryText = Color(0xFFFFFFFF),
            tintColor = Color(0xFFAFBBF7),
            errorColor = Color(0xFFD32F2F)
        ),
        darkPalette = ThemePalette(
            primaryBackground = Color(0xFF1E1E1E),
            primaryText = Color(0xFFFFFFFF),
            secondaryBackground = Color(0xFF141414),
            secondaryText = Color(0xFF777777),
            tertiaryText = Color(0xFFFFFFFF),
            tintColor = Color(0xFF3045C5),
            errorColor = Color(0xFFFF4D6D)
        )
    ),
    TELEGRAM(
        label = "Telegram",
        lightPalette = ThemePalette(
            primaryBackground = Color(0xFFFFFFFF),
            primaryText = Color(0xFF000000),
            secondaryBackground = Color(0xFFF1F1F1),
            secondaryText = Color(0xFFA2AEB8),
            tertiaryText = Color(0xFFFFFFFF),
            tintColor = Color(0xFF419FD9),
            errorColor = Color(0xFFD32F2F)
        ),
        darkPalette = ThemePalette(
            primaryBackground = Color(0xFF182533),
            primaryText = Color(0xFFFFFFFF),
            secondaryBackground = Color(0xFF0E1621),
            secondaryText = Color(0xFF6D7F8F),
            tertiaryText = Color(0xFFFFFFFF),
            tintColor = Color(0xFF2B5278),
            errorColor = Color(0xFFFF4D6D)
        )
    ),
    WHATSAPP(
        label = "WhatsApp",
        lightPalette = ThemePalette(
            primaryBackground = Color(0xFFFFFFFF),
            primaryText = Color(0xFF000000),
            secondaryBackground = Color(0xFFF7F0E6),
            secondaryText = Color(0xFF667781),
            tertiaryText = Color(0xFFFFFFFF),
            tintColor = Color(0xFF25D366),
            errorColor = Color(0xFFD32F2F)
        ),
        darkPalette = ThemePalette(
            primaryBackground = Color(0xFF121B22),
            primaryText = Color(0xFFFFFFFF),
            secondaryBackground = Color(0xFF1F2C34),
            secondaryText = Color(0xFF8696A0),
            tertiaryText = Color(0xFFFFFFFF),
            tintColor = Color(0xFF128C7E),
            errorColor = Color(0xFFFF4D6D)
        )
    ),
    DISCORD(
        label = "Discord",
        lightPalette = ThemePalette(
            primaryBackground = Color(0xFFFFFFFF),
            primaryText = Color(0xFF000000),
            secondaryBackground = Color(0xFFE3E5E8),
            secondaryText = Color(0xFF8A8F98),
            tertiaryText = Color(0xFFFFFFFF),
            tintColor = Color(0xFF5865F2),
            errorColor = Color(0xFFD32F2F)
        ),
        darkPalette = ThemePalette(
            primaryBackground = Color(0xFF313338),
            primaryText = Color(0xFFFFFFFF),
            secondaryBackground = Color(0xFF2B2D31),
            secondaryText = Color(0xFFB9BBBE),
            tertiaryText = Color(0xFFFFFFFF),
            tintColor = Color(0xFF5865F2),
            errorColor = Color(0xFFFF4D6D)
        )
    ),
    IMESSAGE(
        label = "iMessage",
        lightPalette = ThemePalette(
            primaryBackground = Color(0xFFFFFFFF),
            primaryText = Color(0xFF000000),
            secondaryBackground = Color(0xFFE5E5EA),
            secondaryText = Color(0xFF8E8E93),
            tertiaryText = Color(0xFFFFFFFF),
            tintColor = Color(0xFF007AFF),
            errorColor = Color(0xFFD32F2F)
        ),
        darkPalette = ThemePalette(
            primaryBackground = Color(0xFF1C1C1E),
            primaryText = Color(0xFFFFFFFF),
            secondaryBackground = Color(0xFF2C2C2E),
            secondaryText = Color(0xFF8E8E93),
            tertiaryText = Color(0xFFFFFFFF),
            tintColor = Color(0xFF0A84FF),
            errorColor = Color(0xFFFF4D6D)
        )
    )
}

data class ThemePalette(
    val primaryBackground: Color,
    val primaryText: Color,
    val secondaryBackground: Color,
    val secondaryText: Color,
    val tertiaryText: Color,
    val tintColor: Color,
    val errorColor: Color
)