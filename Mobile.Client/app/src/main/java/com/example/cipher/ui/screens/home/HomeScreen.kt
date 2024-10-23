package com.example.cipher.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
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
    var useTopInnerPadding by remember {
        mutableStateOf(true)
    }
    Scaffold (
        topBar = {
            HomeTopAppBar(
                navController,
                onTopPaddingChange = {
                    useTopInnerPadding = it
                })
            },
        bottomBar = {
            HomeNavigationBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(
                    top = if (useTopInnerPadding) innerPadding.calculateTopPadding() else 0.dp,
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            HomeNavGraph(navController = navController)
        }
    }
}

