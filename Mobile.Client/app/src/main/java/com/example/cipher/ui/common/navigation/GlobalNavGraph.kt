package com.example.cipher.ui.common.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cipher.ui.screens.auth.AuthScreen
import com.example.cipher.ui.screens.home.HomeScreen
import com.example.cipher.ui.screens.splash.SplashScreen
import kotlin.reflect.KType

@Composable
fun GlobalNavGraph(
    navController: NavHostController,
    startDestination: GlobalNavScreens
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        navComposable<GlobalNavScreens.AuthScreen>(
            enterTransition = {
               slideInVertically(
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

        navComposable<GlobalNavScreens.SplashScreen>(
            enterTransition = {
                fadeIn(tween(0))
            },
            exitTransition = {
                fadeOut(tween(700))
            }
        ) {
            SplashScreen(
                mainNavController = navController,
                GlobalNavScreens.HomeScreen
            )
        }

        navComposable<GlobalNavScreens.HomeScreen> {
            HomeScreen()
        }

    }
}

inline fun <reified T : NavScreen> NavGraphBuilder.navComposable(
    typeMap: Map<KType, NavType<*>> = emptyMap(),
    noinline enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = { defaultEnterTransition() },
    noinline exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = { defaultExitTransition() },
    noinline content: @Composable (NavBackStackEntry) -> Unit
) {
    composable<T>(
        typeMap = typeMap,
        enterTransition = enterTransition,
        exitTransition = exitTransition
    ) {
        content(it)
    }
}

fun defaultEnterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(durationMillis = 0)
    )
}

fun defaultExitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(durationMillis = 0)
    )
}


