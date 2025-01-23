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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.domain.models.message.Message
import com.example.cipher.ui.common.composable.bubble.model.ArrowAlignment
import com.example.cipher.ui.common.composable.bubble.model.ArrowShape
import com.example.cipher.ui.common.composable.bubble.model.BubbleCornerRadius
import com.example.cipher.ui.common.composable.bubble.model.bubble
import com.example.cipher.ui.common.composable.bubble.model.rememberBubbleState
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import java.time.format.DateTimeFormatter

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    isLocalUser: Boolean = true,
    isFirstInSequence: Boolean = false,
    isMiddleInSequence: Boolean = false,
    isLastInSequence: Boolean = true,
    message: Message
) {
    val bubbleColor = if (isLocalUser) colors.tintColor
    else colors.primaryBackground

    val bubbleTextColor = if (isLocalUser) colors.tertiaryText
    else colors.primaryText

    val sentAt = remember {
        DateTimeFormatter.ofPattern("HH:mm")
            .format(message.createdAt)
    }

    val bubbleAlignment = when {
        isFirstInSequence && isLastInSequence ->
            if (isLocalUser) ArrowAlignment.RightBottom else ArrowAlignment.LeftBottom
        isFirstInSequence ->
            if (isLocalUser) ArrowAlignment.RightBottom else ArrowAlignment.LeftBottom
        isLastInSequence || isMiddleInSequence -> ArrowAlignment.None
        else -> ArrowAlignment.None
    }

    val bubbleCorners = when {
        isMiddleInSequence -> BubbleCornerRadius(
            topLeft = 8.dp,
            topRight = 8.dp,
            bottomLeft = 8.dp,
            bottomRight = 8.dp
        )
        isLastInSequence -> BubbleCornerRadius(
            topLeft = 12.dp,
            topRight = 12.dp,
            bottomLeft = 8.dp,
            bottomRight = 8.dp
        )
        else -> BubbleCornerRadius(
            topLeft = 8.dp,
            topRight = 8.dp,
            bottomLeft = 8.dp,
            bottomRight = 8.dp
        )
    }

    val bubbleState = rememberBubbleState(
        cornerRadius = bubbleCorners,
        alignment = bubbleAlignment,
        arrowShape = ArrowShape.HalfTriangle,
        arrowOffsetX = 0.dp,
        arrowOffsetY = 0.dp,
        arrowWidth = 8.dp,
        arrowHeight = 8.dp,
        drawArrow = true
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
                style = typography.body,
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
                    .copy(fontSize = 10.sp),
                color = colors.secondaryText
            )
        }

    }
}

