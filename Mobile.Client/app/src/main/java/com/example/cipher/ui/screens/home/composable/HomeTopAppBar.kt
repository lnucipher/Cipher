package com.example.cipher.ui.screens.home.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.screens.home.composable.drawer.model.NavigationDrawerState
import com.example.cipher.ui.screens.home.composable.drawer.model.isOpened


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    title: String,
    containerColor: Color? = null,
    contentColor: Color? = null,
    drawerState: NavigationDrawerState,
    onDrawerToggle: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = containerColor ?: colors.primaryBackground,
            titleContentColor = contentColor ?: colors.primaryText,
        ),
        title = {
            Text(
                text = title,
                style = typography.toolbar
            )
        },
        navigationIcon = {
            IconButton(onClick = onDrawerToggle) {
                Icon(
                    imageVector = if (drawerState.isOpened()) Icons.AutoMirrored.Default.ArrowBack
                    else Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = contentColor ?: colors.primaryText
                )
            }
        }
    )
}