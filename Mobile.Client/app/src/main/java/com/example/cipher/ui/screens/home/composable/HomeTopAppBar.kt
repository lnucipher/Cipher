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
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.screens.home.composable.drawer.model.CustomDrawerState
import com.example.cipher.ui.screens.home.composable.drawer.model.isOpened


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    title: String,
    drawerState: CustomDrawerState,
    onDrawerClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colors.primaryBackground,
            titleContentColor = colors.primaryText,
        ),
        title = {
            Text(
                text = title,
                style = typography.toolbar
            )
        },
        navigationIcon = {
            IconButton(onClick = { onDrawerClick() }) {
                Icon(
                    imageVector = if (drawerState.isOpened()) Icons.AutoMirrored.Default.ArrowBack
                    else Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = colors.primaryText
                )
            }
        }
    )
}