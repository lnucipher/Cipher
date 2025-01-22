package com.example.cipher.ui.screens.home.chats.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsTopAppBar() {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colors.primaryBackground,
            titleContentColor = colors.primaryText,
        ),
        title = {
            Text(
                text = "Chats",
                style = typography.toolbar
            )
        }
    )
}