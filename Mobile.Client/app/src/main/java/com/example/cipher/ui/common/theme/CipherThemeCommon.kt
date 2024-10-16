package com.example.cipher.ui.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.cipher.R

data class CipherColors(
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val tertiaryText: Color,
    val tintColor: Color,
    val errorColor: Color,
)

data class CipherTypography(
    val heading: TextStyle,
    val body: TextStyle,
    val toolbar: TextStyle,
    val caption: TextStyle
)

data class CipherShape(
    val componentShape: Shape
)

data class CipherImages(
    val logo: Painter
)

object CipherFonts {

    val manropeFamily: FontFamily by lazy {
        FontFamily(
            Font(R.font.manrope_font_light, FontWeight.Light),
            Font(R.font.manrope_font_regular, FontWeight.Normal),
            Font(R.font.manrope_font_medium, FontWeight.Medium),
            Font(R.font.manrope_font_bold, FontWeight.Bold)
        )
    }

}

object CipherTheme {

    internal val colors: CipherColors
        @Composable @ReadOnlyComposable get() = LocalCipherColors.current

    internal val typography: CipherTypography
        @Composable @ReadOnlyComposable get() = LocalCipherTypography.current

    internal val shapes: CipherShape
        @Composable @ReadOnlyComposable get() = LocalCipherShape.current

    internal val images: CipherImages
        @Composable @ReadOnlyComposable get() = LocalCipherImages.current

    internal var darkTheme: Boolean = false
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

internal val LocalCipherImages = staticCompositionLocalOf<CipherImages> {
    error("no images provided")
}

