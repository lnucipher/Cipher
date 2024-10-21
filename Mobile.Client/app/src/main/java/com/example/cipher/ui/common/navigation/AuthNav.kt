package com.example.cipher.ui.common.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cipher.ui.screens.auth.AuthViewModel
import com.example.cipher.ui.screens.auth.login.LoginScreen
import com.example.cipher.ui.screens.auth.register.AdditionalInfoScreen
import com.example.cipher.ui.screens.auth.register.SignUpScreen

@Composable
fun AuthNav(
    isImeVisible: Boolean,
    maxUpperSectionRatio: MutableState<Float>,
    authViewModel: AuthViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AuthNavScreens.LoginScreen
    ) {

        composable <AuthNavScreens.LoginScreen>(
            enterTransition = {
                return@composable slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(1000)
                )
            },
            exitTransition = {
                return@composable fadeOut(tween(700))
            },
            popEnterTransition = {
                return@composable slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(1000)
                )
            }
        ) {
            LoginScreen (
                navController = navController,
                authViewModel = authViewModel,
                maxUpperSectionRatio = maxUpperSectionRatio
            )
        }

        composable <AuthNavScreens.SignUpScreen>(
            enterTransition = {
                return@composable slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(1000)
                )
            },
            exitTransition = {
                return@composable fadeOut(tween(700))
            },
            popEnterTransition = {
                return@composable slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(1000)
                )
            }
        ) {
            SignUpScreen(
                navController = navController,
                authViewModel = authViewModel,
                maxUpperSectionRatio = maxUpperSectionRatio
            )
        }

        composable <AuthNavScreens.AdditionalInfoScreen>(
            enterTransition = {
                return@composable slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(1000)
                )
            },
            exitTransition = {
                return@composable fadeOut(tween(700))
            },
            popEnterTransition = {
                return@composable slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(1000)
                )
            }
        ) {
            AdditionalInfoScreen(
                isImeVisible = isImeVisible,
                navController = navController,
                authViewModel = authViewModel,
                maxUpperSectionRatio = maxUpperSectionRatio
            )
        }

    }
}

