package com.example.cipher.ui.screens.home.chats.composable

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.screens.home.composable.HomeTopAppBar
import com.example.cipher.ui.screens.home.composable.drawer.model.CustomDrawerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsTopAppBar(
    multiSectionEnabled: Boolean,
    itemsSelected: Set<String>,
    drawerState: CustomDrawerState,
    onMute: () -> Unit,
    onCancel: () -> Unit,
    onDrawerClick: () -> Unit,
    onDelete: (Set<String>) -> Unit
) {
    Crossfade(targetState = multiSectionEnabled, animationSpec = tween(durationMillis = 500)) { isEnabled ->
        if (isEnabled) {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colors.primaryBackground,
                    titleContentColor = colors.primaryText,
                ),
                title = {
                    Text(
                        text = "${itemsSelected.size}",
                        style = typography.toolbar
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onCancel() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = colors.primaryText
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onMute() }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = colors.primaryText
                        )
                    }
                    IconButton(onClick = { onDelete(itemsSelected) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = colors.primaryText
                        )
                    }
                }
            )
        } else {
            HomeTopAppBar(
                title = "Chats",
                onDrawerClick = onDrawerClick,
                drawerState = drawerState
            )
        }
    }
}