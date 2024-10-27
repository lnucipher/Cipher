package com.example.cipher.ui.screens.home.chats.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography

@Composable
fun SearchListItem(
    user: User,
    modifier: Modifier = Modifier
) {
    val bottomBorderColor: Color = colors.secondaryBackground
    Row(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .drawBehind {
                drawLine(
                    color = bottomBorderColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(horizontal = 4.dp)
            .padding(vertical = 4.dp)
    ) {
        Box (
            modifier = Modifier
                .fillMaxHeight()
                .padding(4.dp)
                .fillMaxWidth(0.15f)
        ) {
            Box(
                modifier = Modifier.size(35.dp)
            ) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = user.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            }

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .weight(1f)
        ) {
            Text(
                text = user.name,
                style = typography.body,
                color = colors.primaryText
            )
        }
    }
}