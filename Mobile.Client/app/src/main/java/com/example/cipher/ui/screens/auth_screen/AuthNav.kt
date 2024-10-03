package com.example.cipher.ui.screens.auth_screen

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cipher.ui.screens.auth_screen.login_screen.LoginScreen
import com.example.cipher.ui.screens.auth_screen.register_screen.AdditionalInfoScreen
import com.example.cipher.ui.screens.auth_screen.register_screen.SignUpScreen

@Composable
fun AuthNav(
    isImeVisible: Boolean,
    maxUpperSectionRatio: MutableState<Float>,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.Login.name
    ) {


        composable(AuthRoutes.Login.name, enterTransition = {
            return@composable slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(1000)
            )
        }, exitTransition = {
            return@composable fadeOut(tween(700))
        }, popEnterTransition = {
            return@composable slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(1000)
            )
        }) {
            LoginScreen (
                maxUpperSectionRatio = maxUpperSectionRatio,
                navigateToSignUp = { navController.navigate(AuthRoutes.SignUp.name) }
            )
        }

        composable(AuthRoutes.SignUp.name, enterTransition = {
            return@composable slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(1000)
            )
        }, exitTransition = {
            return@composable fadeOut(tween(700))
        },  popEnterTransition = {
            return@composable slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(1000)
            )
        }) {
            SignUpScreen(
                maxUpperSectionRatio = maxUpperSectionRatio,
                navigateToAdditionalInfo = { navController.navigate(AuthRoutes.AdditionalInfo.name) },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(AuthRoutes.AdditionalInfo.name, enterTransition = {
            return@composable slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(1000)
            )
        }, exitTransition = {
            return@composable fadeOut(tween(700))
        }, popEnterTransition = {
            return@composable slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(1000)
            )
        }) {
            AdditionalInfoScreen(
                isImeVisible = isImeVisible,
                maxUpperSectionRatio = maxUpperSectionRatio,
                navigateToChatsScreen = {  },
                navigateBack = { navController.popBackStack() }
            )
        }

    }
}

enum class AuthRoutes {
    Login, SignUp, AdditionalInfo
}