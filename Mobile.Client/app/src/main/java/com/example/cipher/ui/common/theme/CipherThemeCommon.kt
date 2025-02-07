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
import com.example.cipher.domain.models.settings.ThemePalette

data class CipherColors(
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val tertiaryText: Color,
    val tintColor: Color,
    val errorColor: Color,
)
fun ThemePalette.toCipherColors(): CipherColors {
    return CipherColors(
        primaryBackground = primaryBackground,
        primaryText = primaryText,
        secondaryBackground = secondaryBackground,
        secondaryText = secondaryText,
        tertiaryText = tertiaryText,
        tintColor = tintColor,
        errorColor = errorColor
    )
}

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
    val logo: Painter,
    val noMessagesYet: Painter
)

data class CipherMessageStyle(
    val messageFontSize: Int,
    val messageCornerSize: Int
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

    internal val messageStyle: CipherMessageStyle
        @Composable @ReadOnlyComposable get() = LocalCipherMessageStyle.current

    internal var darkTheme: Boolean = false
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

internal val LocalCipherMessageStyle = staticCompositionLocalOf<CipherMessageStyle> {
    error("no message style provided")
}


