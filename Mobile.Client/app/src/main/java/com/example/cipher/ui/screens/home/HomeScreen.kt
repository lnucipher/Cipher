package com.example.cipher.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cipher.ui.common.navigation.HomeNavGraph
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.screens.home.composable.HomeNavigationBar
import com.example.cipher.ui.screens.home.composable.HomeTopAppBar

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController()
) {
    Scaffold (
        topBar = { HomeTopAppBar(navController) },
        bottomBar = {
            HomeNavigationBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(innerPadding)
        ) {
            HomeNavGraph(navController = navController)
        }
    }
}

