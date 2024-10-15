package com.example.cipher.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cipher.ui.screens.auth_screen.AuthScreen
import com.example.cipher.ui.screens.splash_screen.SplashScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.SplashScreen.name
    ) {
        composable(
            NavRoutes.AuthScreen.name,
            enterTransition = {
                return@composable  slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
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

        composable(
            NavRoutes.SplashScreen.name,
            enterTransition = {
                return@composable slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                return@composable fadeOut(tween(700))
            }
        ) {
            SplashScreen(
                mainNavController = navController
            )
        }
    }
}

enum class NavRoutes {
    AuthScreen, SplashScreen
}