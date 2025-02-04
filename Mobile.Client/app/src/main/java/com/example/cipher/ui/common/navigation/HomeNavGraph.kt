package com.example.cipher.ui.common.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.ui.screens.home.chats.ChatsScreen
import com.example.cipher.ui.screens.home.composable.drawer.model.CustomDrawerState
import com.example.cipher.ui.screens.home.settings.SettingsScreen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    localUser: LocalUser,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = HomeNavScreens.ChatsScreen
    ) {
        navComposable<HomeNavScreens.ChatsScreen>(
            enterTransition = {
                fadeIn(tween(0))
            },
            exitTransition = {
                fadeOut(tween(0))
            }
        ) {
            ChatsScreen(
                localUser = localUser,
                drawerState = drawerState,
                navController = navController,
                onDrawerClick = onDrawerClick
            )
        }

        navComposable<HomeNavScreens.SettingsScreen>(
            enterTransition = {
                fadeIn(tween(0))
            },
            exitTransition = {
                fadeOut(tween(0))
            }
        ) {
            SettingsScreen(
                drawerState = drawerState,
                onDrawerClick = onDrawerClick
            )
        }

        chatNavGraph(navController = navController)
    }
}
