package com.example.cipher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.cipher.ui.screens.auth_screen.AuthScreen
import com.example.cipher.ui.theme.CipherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CipherTheme (
                darkTheme = false
            ) {
                val navController: NavHostController = rememberNavController()
                SharedTransitionScope {
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.AuthRoute.name
                    ) {
                        composable(NavRoutes.AuthRoute.name) {
                            AuthScreen(
                                mainNavController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class NavRoutes {
    AuthRoute
}