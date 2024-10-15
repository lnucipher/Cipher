package com.example.cipher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.cipher.ui.navigation.SetupNavGraph
import com.example.cipher.ui.screens.auth_screen.AuthScreen
import com.example.cipher.ui.screens.splash_screen.SplashScreen
import com.example.cipher.ui.theme.CipherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            CipherTheme () {
                val navController: NavHostController = rememberNavController()
                SharedTransitionScope {
                    SetupNavGraph(navController = navController)
                }
            }
        }
    }
}
