package com.example.cipher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cipher.ui.common.navigation.GlobalNavGraph
import com.example.cipher.ui.common.theme.CipherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            CipherTheme (darkTheme = false) {
                val navController: NavHostController = rememberNavController()
                GlobalNavGraph(navController = navController)
            }
        }
    }
}
