package com.example.cipher.ui.screens.home.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.screens.home.composable.HomeTopAppBar
import com.example.cipher.ui.screens.home.composable.drawer.model.CustomDrawerState
import com.example.cipher.ui.screens.home.composable.drawer.model.opposite

@Composable
fun SettingsScreen(
    drawerState: CustomDrawerState,
    viewModel: SettingsViewModel = hiltViewModel(),
    onDrawerClick: (CustomDrawerState) -> Unit
) {
    val context = LocalContext.current
    Scaffold (
        topBar = {
            HomeTopAppBar(
                title = "Settings",
                onDrawerClick = { onDrawerClick(drawerState.opposite()) },
                drawerState = drawerState
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(innerPadding)
                .padding(12.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Settings", style = typography.heading, color = colors.primaryText)
            Button(onClick = { viewModel.logout(context) }) {
                Text(text = "Logout", style = typography.heading, color = colors.primaryText)
            }
        }
    }
}