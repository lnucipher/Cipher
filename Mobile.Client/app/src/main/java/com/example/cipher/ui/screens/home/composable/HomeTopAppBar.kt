package com.example.cipher.ui.screens.home.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cipher.R
//import com.example.cipher.ui.common.navigation.BottomBarScreens
import com.example.cipher.ui.common.navigation.HomeNavScreens
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    navController: NavController,
    onTopPaddingChange: (Boolean) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val title = when (currentRoute) {
//        HomeNavScreens.ChatsScreen::class.qualifiedName -> BottomBarScreens.Chats.title
//        HomeNavScreens.ProfileScreen::class.qualifiedName -> BottomBarScreens.Profile.title
//        HomeNavScreens.SettingsScreen::class.qualifiedName -> BottomBarScreens.Settings.title
        else -> "Cipher"
    }

    val navigationIcon = when (currentRoute) {
        HomeNavScreens.ChatsScreen::class.qualifiedName -> null
        HomeNavScreens.ProfileScreen::class.qualifiedName -> painterResource(R.drawable.arrow_back_ios_icon)
        HomeNavScreens.SettingsScreen::class.qualifiedName -> painterResource(R.drawable.arrow_back_ios_icon)
        else -> null
    }

    val topBarDestination = listOf(
        HomeNavScreens.ChatsScreen::class.qualifiedName,
        HomeNavScreens.SettingsScreen::class.qualifiedName
    ).contains(currentRoute)

    onTopPaddingChange(topBarDestination)

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(initialOffsetY = { -it }),
        exit = fadeOut(animationSpec = tween(700)) + slideOutVertically(targetOffsetY = { -it })
    ) {
        TopAppBar(

            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colors.primaryBackground,
                titleContentColor = colors.primaryText,
            ),
            title = {
                Text(
                    text = title,
                    style = typography.toolbar
                )
            },
            navigationIcon = {
                navigationIcon?.let { icon ->
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = icon,
                            contentDescription = null,
                            tint = colors.primaryText
                        )
                    }
                }
            }
        )
    }
}