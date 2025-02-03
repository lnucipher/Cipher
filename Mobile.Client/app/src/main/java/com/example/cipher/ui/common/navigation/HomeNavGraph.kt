package com.example.cipher.ui.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.ui.screens.home.chats.ChatsScreen
import com.example.cipher.ui.screens.home.settings.SettingsScreen

@Composable
fun HomeNavGraph(navController: NavHostController, localUser: LocalUser) {
    NavHost(
        navController = navController,
        startDestination = HomeNavScreens.ChatsScreen
    ) {
        navComposable<HomeNavScreens.ChatsScreen> {
            ChatsScreen(
                localUser = localUser,
                navController = navController
            )
        }

        navComposable<HomeNavScreens.SettingsScreen> {
            SettingsScreen()
        }

        chatNavGraph(navController = navController)
    }
}
