package com.example.cipher.ui.common.navigation

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
import com.example.cipher.ui.screens.home.HomeScreen
import com.example.cipher.ui.screens.home.chat.PersonalChatScreen
import kotlin.reflect.typeOf

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = GlobalNavScreens.HomeScreen
    ) {
        composable<GlobalNavScreens.HomeScreen>(
            enterTransition = {
                fadeIn(tween(400))
            },
            exitTransition = {
                fadeOut(tween(600))
            }
        ) {
            HomeScreen(navController = navController)
        }

        composable<ChatNavScreens.PersonalChatScreen>(
            typeMap = mapOf(
                typeOf<User>() to UserType,
                typeOf<LocalUser>() to LocalUserType
            ),
            enterTransition = {
                fadeIn(tween(400))
            },
            exitTransition = {
                fadeOut(tween(600))
            }
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<ChatNavScreens.PersonalChatScreen>()
            PersonalChatScreen(
                navController = navController,
                localUser = args.localUser,
                contact = args.contact
            )
        }
    }
}
