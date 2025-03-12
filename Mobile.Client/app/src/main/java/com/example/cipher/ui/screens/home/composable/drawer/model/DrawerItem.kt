package com.example.cipher.ui.screens.home.composable.drawer.model

import com.example.cipher.R

enum class DrawerItem (
    val title: String,
    val icon: Int
) {
    Chats(
        icon = R.drawable.chats_icon,
        title = "Chats"
    ),
    Settings(
        icon = R.drawable.settings_icon,
        title = "Settings"
    ),
    Logout(
        icon = R.drawable.logout_icon,
        title = "Logout"
    )
}