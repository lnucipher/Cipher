package com.example.cipher.ui.screens.home.chat.composable

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.domain.models.message.Message
import com.example.cipher.ui.common.composable.bubble.model.ArrowAlignment
import com.example.cipher.ui.common.composable.bubble.model.ArrowShape
import com.example.cipher.ui.common.composable.bubble.model.BubbleCornerRadius
import com.example.cipher.ui.common.composable.bubble.model.bubble
import com.example.cipher.ui.common.composable.bubble.model.rememberBubbleState
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.messageStyle
import com.example.cipher.ui.common.theme.CipherTheme.typography
import java.time.format.DateTimeFormatter

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    isLocalUser: Boolean = true,
    isFirstInSequence: Boolean = false,
    isMiddleInSequence: Boolean = false,
    containerColor: Color? = null,
    textColor: Color? = null,
    message: Message
) {
    val bubbleColor = containerColor ?: if (isLocalUser) colors.tintColor else colors.primaryBackground
    val bubbleTextColor = textColor ?: if (isLocalUser) colors.tertiaryText else colors.primaryText
    val sentAt = remember { DateTimeFormatter.ofPattern("HH:mm").format(message.createdAt) }

    val bubbleAlignment = when {
        isFirstInSequence -> if (isLocalUser) ArrowAlignment.RightBottom else ArrowAlignment.LeftBottom
        else -> ArrowAlignment.None
    }

    val cornerSize = messageStyle.messageCornerSize
    val smallCornerSize = cornerSize - 4
    val fontSize = messageStyle.messageFontSize
    val smallFontSize = fontSize - 6

    val bubbleCorners = BubbleCornerRadius(
        topLeft = if (isMiddleInSequence) smallCornerSize.dp else cornerSize.dp,
        topRight = if (isMiddleInSequence) smallCornerSize.dp else cornerSize.dp,
        bottomLeft = smallCornerSize.dp,
        bottomRight = smallCornerSize.dp
    )

    val bubbleState = rememberBubbleState(
        cornerRadius = bubbleCorners,
        alignment = bubbleAlignment,
        arrowShape = ArrowShape.HalfTriangle,
        arrowWidth = 8.dp,
        arrowHeight = 8.dp,
        drawArrow = isFirstInSequence
    )

    Row (
        modifier = modifier
            .widthIn()
            .height(IntrinsicSize.Max)
            .bubble(bubbleState, color = bubbleColor)
            .padding(4.dp)
    ) {
        Spacer(modifier = Modifier.width(2.dp))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, false)
                .wrapContentWidth()
                .padding(2.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = message.text,
                style = typography.body.copy(
                    fontSize = fontSize.sp
                ),
                color = bubbleTextColor,
                modifier = Modifier.wrapContentHeight()
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = sentAt,
                style = typography.caption
                    .copy(fontSize = smallFontSize.sp),
                color = colors.secondaryText
            )
        }
    }
}