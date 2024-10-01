package com.example.cipher.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.example.cipher.R

data class CipherColors(
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val tintColor: Color,
    val errorColor: Color,
)

data class CipherTypography(
    val heading: TextStyle,
    val body: TextStyle,
    val toolbar: TextStyle,
    val caption: TextStyle
)

object CipherFonts {

    val generalSansFamily: FontFamily by lazy {
        FontFamily(
            Font(R.font.general_sans_font_light, FontWeight.Light),
            Font(R.font.general_sans_font_regular, FontWeight.Normal),
            Font(R.font.general_sans_font_medium, FontWeight.Medium),
            Font(R.font.general_sans_font_bold, FontWeight.Bold)
        )
    }
}


data class CipherShape(
    val padding: CipherPadding,
    val cornersStyle: Shape
)

data class CipherPadding(
    val defaultPadding: Dp,
    val extraSmallPadding: Dp,
    val smallPadding: Dp,
    val mediumPadding: Dp,
    val bigPadding: Dp,
)

object CipherTheme {

    internal val colors: CipherColors
        @Composable @ReadOnlyComposable get() = LocalCipherColors.current

    internal val typography: CipherTypography
        @Composable @ReadOnlyComposable get() = LocalCipherTypography.current

    internal val shapes: CipherShape
        @Composable @ReadOnlyComposable get() = LocalCipherShape.current

}

enum class CipherStyle {
    Default
}


internal val LocalCipherColors = staticCompositionLocalOf<CipherColors> {
    error("no colors provided")
}

internal val LocalCipherTypography = staticCompositionLocalOf<CipherTypography> {
    error("no font provided")
}

internal val LocalCipherShape = staticCompositionLocalOf<CipherShape> {
    error("no shapes provided")
}
