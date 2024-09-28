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
    textSize: CipherSize = CipherSize.Medium,
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

    val typography = CipherTypography(
        heading = TextStyle(
            fontSize = when (textSize) {
                CipherSize.Small -> 24.sp
                CipherSize.Medium -> 28.sp
                CipherSize.Big -> 32.sp
            },
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = CipherFonts.generalSansFamily
        ),
        body = TextStyle(
            fontSize = when (textSize) {
                CipherSize.Small -> 14.sp
                CipherSize.Medium -> 16.sp
                CipherSize.Big -> 18.sp
            },
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = CipherFonts.generalSansFamily
        ),
        toolbar = TextStyle(
            fontSize = when (textSize) {
                CipherSize.Small -> 14.sp
                CipherSize.Medium -> 16.sp
                CipherSize.Big -> 18.sp
            },
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = CipherFonts.generalSansFamily
        ),
        caption = TextStyle(
            fontSize = when (textSize) {
                CipherSize.Small -> 10.sp
                CipherSize.Medium -> 12.sp
                CipherSize.Big -> 14.sp
            },
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight.Light,
            fontFamily = CipherFonts.generalSansFamily
        ),
    )

    val shapes = CipherShape(
        padding = CipherPadding(
            defaultPadding = 0.dp,
            extraSmallPadding = 4.dp,
            smallPadding = 8.dp,
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