package com.example.cipher.ui.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cipher.ui.screens.home.chats.ChatsScreen
import com.example.cipher.ui.screens.home.profile.ProfileScreen
import com.example.cipher.ui.screens.home.settings.SettingsScreen

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeNavScreens.ChatsScreen
    ) {
        composable<HomeNavScreens.ChatsScreen>
        {
            ChatsScreen()
        }
        composable <HomeNavScreens.ProfileScreen>
        {
            ProfileScreen()
        }
        composable <HomeNavScreens.SettingsScreen>
        {
            SettingsScreen()
        }
    }
}
