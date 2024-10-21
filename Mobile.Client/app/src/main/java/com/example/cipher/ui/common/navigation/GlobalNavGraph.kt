package com.example.cipher.ui.common.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cipher.ui.screens.auth.AuthScreen
import com.example.cipher.ui.screens.home.HomeScreen

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
                fadeOut(tween(700))
            }
        ) {
            AuthScreen(
                mainNavController = navController
            )
        }

        composable <GlobalNavScreens.HomeScreen>
        {
            HomeScreen()
        }
    }
}

