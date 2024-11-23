package com.example.cipher.ui.screens.home.profile.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.cipher.R
import com.example.cipher.data.NetworkKeys
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.common.utils.LastSeenFormatter

@Composable
fun ProfileInfoPopup(
    imageLoader: ImageLoader,
    user: User,
    onDismissRequest: () -> Unit
) {
    val scrollState = rememberScrollState()
    Popup(
        onDismissRequest = { onDismissRequest() },
        alignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = colors.primaryBackground,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { onDismissRequest() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.close_icon),
                        tint = colors.primaryText,
                        contentDescription = null
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .wrapContentWidth()
                        .wrapContentHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(
                                    BorderStroke(2.dp, colors.primaryText),
                                    CircleShape
                                )
                        ) {
                            AsyncImage(
                                model = NetworkKeys.IDENTITY_SERVER_BASE_URL + user.avatarUrl,
                                contentDescription = null,
                                imageLoader = imageLoader,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = user.name,
                                color = colors.primaryText,
                                style = typography.body.copy(fontSize = 18.sp),
                            )
                            Text(
                                text = if (user.status == Status.ONLINE) user.status.name else LastSeenFormatter.getLastSeenMessage(user.lastSeen),
                                color = colors.secondaryText,
                                style = typography.body,
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            modifier = Modifier
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ProfileListItem(
                                title = "Username",
                                value = "@${user.username}"
                            )
                            ProfileListItem(
                                title = "Birthdate",
                                value = user.birthDate
                            )
                            ProfileListItem(
                                modifier = Modifier,
                                title = "Bio",
                                value = user.bio
                            )
                        }
                    }
                }
            }
        }
    }
}
