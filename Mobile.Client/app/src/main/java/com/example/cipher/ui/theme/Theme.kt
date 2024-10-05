package com.example.cipher.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.R


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
            fontWeight = FontWeight.Medium
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
        componentShape = RoundedCornerShape(8.dp)
    )

    val images = CipherImages(
        logo = if (darkTheme) painterResource(R.drawable.cipher_logo_dark) else painterResource(R.drawable.cipher_logo_light)
    )

    CompositionLocalProvider(
        LocalCipherColors provides colors,
        LocalCipherTypography provides typography,
        LocalCipherShape provides shapes,
        LocalCipherImages provides  images,
        content = content
    )


}