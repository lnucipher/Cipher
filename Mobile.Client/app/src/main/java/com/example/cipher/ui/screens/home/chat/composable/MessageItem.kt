package com.example.cipher.ui.screens.home.chat.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.domain.models.message.Message
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    isLocalUser: Boolean = true,
    message: Message
) {
    val bubbleShape = if (isLocalUser) {
        RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
            bottomStart = 8.dp,
            bottomEnd = 2.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
            bottomStart = 2.dp,
            bottomEnd = 8.dp
        )
    }

    val bubbleColor = if (isLocalUser) colors.tintColor
    else colors.primaryBackground

    val bubbleTextColor = if (isLocalUser) colors.tertiaryText
    else colors.primaryText

    val sentAt = DateTimeFormatter.ofPattern("HH:mm")
        .format(message.createdAt)

    Row (
        modifier = modifier
            .widthIn()
            .height(IntrinsicSize.Max)
            .background(
                color = bubbleColor,
                shape = bubbleShape
            )
            .padding(4.dp)
    ) {
        Spacer(modifier = Modifier.width(2.dp))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(2.dp)
                .weight(5f, false),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = message.text,
                style = typography.body,
                color = bubbleTextColor,
                modifier = Modifier.wrapContentHeight()
            )
        }

        Spacer(modifier = Modifier.width(2.dp))

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f, true),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = sentAt,
                style = typography.caption
                    .copy(fontSize = 7.sp),
                color = colors.secondaryText
            )
        }

    }
}


