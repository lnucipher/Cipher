package com.example.cipher.ui.common.theme


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
import com.example.cipher.domain.models.settings.Settings
import com.example.cipher.ui.common.theme.CipherTheme.darkTheme


@Composable
fun CipherTheme(
    settings: Settings,
    content: @Composable () -> Unit
) {
    darkTheme = settings.darkTheme ?: isSystemInDarkTheme()

    val colors = when(darkTheme) {
        true -> settings.theme.darkPalette
        false -> settings.theme.lightPalette
    }.toCipherColors()

    val baseTextStyle = TextStyle(
        fontFamily = CipherFonts.manropeFamily,
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
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
        caption = baseTextStyle.copy(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    )

    val shapes = CipherShape(
        componentShape = RoundedCornerShape(8.dp)
    )

    val messageStyle = CipherMessageStyle(
        messageFontSize = settings.messageFontSize,
        messageCornerSize = settings.messageCornersSize
    )

    val images = CipherImages(
        logo = if (darkTheme) painterResource(R.drawable.cipher_logo_dark) else painterResource(R.drawable.cipher_logo_light),
        noMessagesYet = if (darkTheme) painterResource(R.drawable.no_messages_yet_dark_icon) else painterResource(R.drawable.no_messages_yet_light_icon)
    )

    CompositionLocalProvider(
        LocalCipherColors provides colors,
        LocalCipherTypography provides typography,
        LocalCipherShape provides shapes,
        LocalCipherImages provides  images,
        LocalCipherMessageStyle provides messageStyle,
        content = content
    )

}