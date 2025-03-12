package com.example.cipher.ui.screens.home.composable.drawer

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.screens.home.composable.drawer.composable.DrawerItemView
import com.example.cipher.ui.screens.home.composable.drawer.composable.DrawerProfile
import com.example.cipher.ui.screens.home.composable.drawer.model.DrawerItem

@Composable
fun NavigationDrawer(
    localUser: LocalUser,
    imageLoader: ImageLoader,
    selectedNavigationItem: DrawerItem,
    onNavigationItemClick: (DrawerItem) -> Unit,
    onLogoutItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.9f)
            .fillMaxWidth(fraction = 0.6f)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DrawerProfile(user = localUser, imageLoader = imageLoader,)

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            thickness = (0.5).dp,
            color = colors.primaryText
        )

        DrawerItem.entries.toTypedArray().take(2).forEach { item ->
            DrawerItemView(
                drawerItem = item,
                onClick = {
                    onNavigationItemClick(item)
                },
                selected = selectedNavigationItem == item
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        DrawerItemView(
            drawerItem = DrawerItem.Logout,
            onClick = {
                onLogoutItemClick()
            },
            selected = false
        )
    }
}