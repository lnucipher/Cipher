package com.example.cipher.ui.common.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.screens.auth.AuthScreen
import com.example.cipher.ui.screens.home.HomeScreen
import com.example.cipher.ui.screens.home.chat.PersonalChatScreen
import com.example.cipher.ui.screens.splash.SplashScreen
import kotlin.reflect.typeOf

@Composable
fun GlobalNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = GlobalNavScreens.AuthScreen
    ) {
        composable <GlobalNavScreens.AuthScreen>(
            enterTransition = {
                return@composable  slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(1000)
                )
            },
            exitTransition = {
                fadeOut(tween(1000))
            }
        ) {
            AuthScreen(
                mainNavController = navController
            )
        }
        composable <GlobalNavScreens.SplashScreen>(
            enterTransition = {
                return@composable fadeIn(tween(0))
            },
            exitTransition = {
                return@composable fadeOut(tween(700))
            }
        ) {
            SplashScreen(
                mainNavController = navController,
                GlobalNavScreens.HomeScreen
            )
        }
        composable <GlobalNavScreens.HomeScreen>
        {
            HomeScreen(navController = navController)
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

