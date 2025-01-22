package com.example.cipher.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cipher.ui.common.navigation.HomeNavScreens
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.screens.home.chats.ChatsScreen
import com.example.cipher.ui.screens.home.profile.ProfileScreen
import com.example.cipher.ui.screens.home.settings.SettingsScreen
import androidx.compose.ui.draw.shadow
import com.example.cipher.ui.screens.home.composable.HomeNavigationBar

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val pagerState = rememberPagerState(
        pageCount = { HomeNavScreens.entries.size },
        initialPage = 1
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = pagerState
        ) { page ->
            when (HomeNavScreens.entries[page]) {
                HomeNavScreens.ProfileScreen -> ProfileScreen()
                HomeNavScreens.ChatsScreen -> ChatsScreen(navController)
                HomeNavScreens.SettingsScreen -> SettingsScreen()
            }
        }
        HomeNavigationBar(
            modifier = Modifier
                .fillMaxHeight(0.11f)
                .shadow(
                    elevation = 10.dp
                ),
            pagerState = pagerState,
            scope = scope
        )
    }
}

