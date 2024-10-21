package com.example.cipher.ui.screens.home.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cipher.ui.common.navigation.BottomBarScreens
import com.example.cipher.ui.common.navigation.HomeNavScreens
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val title = when (currentRoute) {
        HomeNavScreens.ChatsScreen::class.qualifiedName -> BottomBarScreens.Chats.title
        HomeNavScreens.ProfileScreen::class.qualifiedName -> BottomBarScreens.Profile.title
        HomeNavScreens.SettingsScreen::class.qualifiedName -> BottomBarScreens.Settings.title
        else -> "Cipher"
    }

    val navigationIcon = when (currentRoute) {
        HomeNavScreens.ChatsScreen::class.qualifiedName -> null
        HomeNavScreens.ProfileScreen::class.qualifiedName -> Icons.AutoMirrored.Filled.ArrowBack
        HomeNavScreens.SettingsScreen::class.qualifiedName -> Icons.AutoMirrored.Filled.ArrowBack
        else -> Icons.AutoMirrored.Filled.ArrowBack
    }

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
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.primaryText
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { /*Add click*/ }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = colors.primaryText
                )
            }
        }
    )
}