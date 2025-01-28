package com.example.cipher.ui.screens.home.chat.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.cipher.R
import com.example.cipher.data.NetworkKeys
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.common.utils.LastSeenFormatter
import com.example.cipher.ui.screens.home.chats.models.ClickedUserStatusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalChatTopAppBar(
    navController: NavController,
    imageLoader: ImageLoader,
    chatCoUser: User,
    onProfileChecker: () -> Unit
) {

    LaunchedEffect(chatCoUser) {
        ClickedUserStatusManager.updateClickedUserStatus(
            userId = chatCoUser.id,
            status = chatCoUser.status,
            lastSeen = chatCoUser.lastSeen
        )
    }
    val coUserStatus by ClickedUserStatusManager.clickedUserStatus.collectAsState()

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colors.primaryBackground,
            titleContentColor = colors.primaryText,
        ),
        title = {
            Column(
                modifier = Modifier.clickable { onProfileChecker() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = chatCoUser.name,
                    style = typography.toolbar,
                    color = colors.primaryText
                )
                Text(
                    text = when (coUserStatus.status) {
                        Status.ONLINE -> coUserStatus.status?.name ?: chatCoUser.status.name
                        else -> LastSeenFormatter.getLastSeenMessage(coUserStatus.lastSeen ?: chatCoUser.lastSeen)
                    },
                    style = typography.body,
                    color = colors.secondaryText
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back_ios_icon),
                    contentDescription = null,
                    tint = colors.primaryText
                )
            }
        },
        actions = {
            OutlinedButton(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(0.dp, Color.Transparent),
                onClick = { onProfileChecker() }
            ) {
                AsyncImage(
                    model = NetworkKeys.IDENTITY_SERVER_BASE_URL +  chatCoUser.avatarUrl,
                    imageLoader = imageLoader,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
}

