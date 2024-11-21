package com.example.cipher.ui.screens.home.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cipher.data.mappers.toUser
import com.example.cipher.ui.screens.home.profile.composable.ProfileInfoScreen
import com.example.cipher.ui.screens.home.profile.composable.ProfileTopBar

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val localUser by viewModel.localUser.collectAsStateWithLifecycle()
    ProfileTopBar {

    }
    Column {
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        ProfileInfoScreen(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            imageLoader = viewModel.imageLoader,
            user = localUser.toUser()
        )
    }
}