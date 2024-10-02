package com.example.cipher.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CipherTheme(
    style: CipherStyle = CipherStyle.Default,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = when(darkTheme) {
        true -> {
            when(style) {
                CipherStyle.Default -> baseDarkPalette
            }
        }
        false -> {
            when(style) {
                CipherStyle.Default -> baseLightPalette
            }
        }
    }

    val baseTextStyle = TextStyle(
        fontFamily = CipherFonts.generalSansFamily,
        letterSpacing = 0.5.sp
    )

    val typography = CipherTypography(
        heading = baseTextStyle.copy(
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        ),
        body = baseTextStyle.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        ),
        toolbar = baseTextStyle.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        ),
        caption = baseTextStyle.copy(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    )

    val shapes = CipherShape(
        padding = CipherPadding(
            defaultPadding = 0.dp,
            extraSmallPadding = 4.dp,
            smallPadding = 8.dp,
            normalPadding = 12.dp,
            mediumPadding = 16.dp,
            bigPadding = 24.dp
        ),
        cornersStyle = RoundedCornerShape(8.dp)
    )

    CompositionLocalProvider(
        LocalCipherColors provides colors,
        LocalCipherTypography provides typography,
        LocalCipherShape provides shapes,
        content = content
    )


}