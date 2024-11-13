package com.example.cipher.ui.screens.home.chats.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.cipher.data.NetworkKeys
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.common.utils.LastSeenFormatter.getLastSeenMessage

@Composable
fun ChatsItem(
    contact: User,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader
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
            .padding(vertical = 16.dp)
    ) {

        Box (
            modifier = Modifier
                .fillMaxHeight()
                .padding(4.dp)
                .fillMaxWidth(0.15f)
        ) {
            Box(
                modifier = Modifier.size(50.dp)
            ) {
                AsyncImage(
                    model = NetworkKeys.IDENTITY_SERVER_BASE_URL + contact.avatarUrl,
                    imageLoader = imageLoader,
                    contentDescription = contact.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
                if (contact.status == Status.ONLINE) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 5.dp, y = (-5).dp)
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                            .border(1.5.dp, Color.White, CircleShape)
                    )
                }
            }

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .weight(1f)
        ) {
            Text(
                text = contact.name,
                style = typography.body,
                color = colors.primaryText
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (contact.status == Status.ONLINE) contact.status.name else getLastSeenMessage(contact.lastSeen),
                style = typography.caption,
                color = colors.secondaryText
            )
        }

    }
    
}

