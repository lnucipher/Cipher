package com.example.cipher.ui.common.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.screens.home.chat.PersonalChatScreen
import com.example.cipher.ui.screens.home.chats.ChatsScreen
import com.example.cipher.ui.screens.home.profile.ProfileScreen
import com.example.cipher.ui.screens.home.settings.SettingsScreen
import kotlin.reflect.typeOf

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeNavScreens.ChatsScreen
    ) {
        composable<HomeNavScreens.ChatsScreen>(
            enterTransition = {
                return@composable fadeIn(
                    tween(500)
                )
            },
            exitTransition = {
                return@composable fadeOut(
                    tween(700)
                )
            },
            popExitTransition = {
                return@composable fadeOut(
                    tween(700)
                )
            }
        ) {
            ChatsScreen(navController)
        }
        composable<HomeNavScreens.ProfileScreen>(
            enterTransition = {
                return@composable fadeIn(
                    tween(700)
                )
            },
            exitTransition = {
                return@composable fadeOut(
                    tween(500)
                )
            },
            popExitTransition = {
                return@composable fadeOut(
                    tween(500)
                )
            }
        ) {
            ProfileScreen()
        }
        composable<HomeNavScreens.SettingsScreen>(
            enterTransition = {
                return@composable fadeIn(
                    tween(500)
                )
            },
            exitTransition = {
                return@composable fadeOut(
                    tween(700)
                )
            },
            popExitTransition = {
                return@composable fadeOut(
                    tween(700)
                )
            }
        ) {
            SettingsScreen()
        }
        composable <ChatNavScreens.PersonalChatScreen>(
            typeMap = mapOf(
                typeOf<User>() to UserType,
                typeOf<LocalUser>() to LocalUserType
            ),
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(500)
                )
            },
            exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(700)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(700)
                )
            }
        )
        {
            val args = it.toRoute<ChatNavScreens.PersonalChatScreen>()
            PersonalChatScreen(
                navController = navController,
                localUser = args.localUser,
                contact = args.contact
            )
        }
    }
}
