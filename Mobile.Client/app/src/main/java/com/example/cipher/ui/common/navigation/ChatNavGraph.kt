package com.example.cipher.ui.common.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.screens.home.chat.PersonalChatScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.chatNavGraph(navController: NavHostController) {
    navComposable<ChatNavScreens.UserProfileScreen> (
        typeMap = mapOf(
            typeOf<User>() to UserType
        )
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<ChatNavScreens.PersonalChatScreen>()
    }

    navComposable<ChatNavScreens.PersonalChatScreen> (
        typeMap = mapOf(
            typeOf<User>() to UserType,
            typeOf<LocalUser>() to LocalUserType
        ),
        enterTransition = {
            fadeIn(tween(300))
        },
        exitTransition = {
            fadeOut(tween(500))
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