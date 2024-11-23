package com.example.cipher.ui.screens.home.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cipher.R
import com.example.cipher.data.mappers.toUser
import com.example.cipher.domain.models.user.EditResult
import com.example.cipher.ui.common.composable.LoadingIndicator
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.screens.auth.composable.ErrorAlertDialog
import com.example.cipher.ui.screens.home.profile.composable.ProfileEditScreen
import com.example.cipher.ui.screens.home.profile.composable.ProfileInfoScreen

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val localUser by viewModel.localUser.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel) {
        viewModel.editResult.collect { result ->
            if (result is EditResult.Success) {
                viewModel.state = viewModel.state.copy(dialogMessage = "Data has been successfully changed")
                viewModel.state = viewModel.state.copy(dialogTitle = "Editing success")
                viewModel.state = viewModel.state.copy(showErrorDialog = true)
                viewModel.state = viewModel.state.copy(isEditing = false)
            } else if (result is EditResult.Error) {
                viewModel.state = viewModel.state.copy(dialogMessage = result.errorMessage)
                viewModel.state = viewModel.state.copy(dialogTitle = "Editing failed")
                viewModel.state = viewModel.state.copy(showErrorDialog = true)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.tintColor.copy(alpha = 1f),
                        colors.tintColor.copy(alpha = 0.9f),
                        colors.tintColor.copy(alpha = 0.8f),
                        colors.tintColor.copy(alpha = 0.7f),
                        colors.tintColor.copy(alpha = 0.6f),
                        colors.tintColor.copy(alpha = 0.5f),
                        colors.tintColor.copy(alpha = 0.4f),
                        colors.tintColor.copy(alpha = 0.3f),
                        colors.tintColor.copy(alpha = 0.2f),
                        colors.tintColor.copy(alpha = 0.1f),
                        colors.primaryBackground.copy(alpha = 1f),
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
                .padding(32.dp)
                .zIndex(1f),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            if (viewModel.state.isEditing) {
                TextButton(
                    border = BorderStroke(1.dp, color = colors.primaryText),
                    onClick = { viewModel.state = viewModel.state.copy(isEditing = false) }
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_ios_icon),
                            contentDescription = "Back",
                            tint = colors.primaryText
                        )
                    }
                }
            }

        }

        Column {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Crossfade(targetState = viewModel.state.isEditing, label = "") { isEditing ->
                if (isEditing) {
                    ProfileEditScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        viewModel = viewModel
                    )
                } else {
                    ProfileInfoScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        imageLoader = viewModel.imageLoader,
                        user = localUser.toUser(),
                        onEditClick = {
                            viewModel.state = viewModel.state.copy(isEditing = true)
                        }
                    )
                }
            }
        }

        if (viewModel.state.showErrorDialog) {
            Box(
                modifier = Modifier.zIndex(2f)
            ){
                ErrorAlertDialog(
                    text = viewModel.state.dialogMessage,
                    title = viewModel.state.dialogTitle
                ) {
                    viewModel.onClear()
                    viewModel.state = viewModel.state.copy(showErrorDialog = false)
                }
            }
        }

        if (viewModel.state.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
                    .zIndex(2f),
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(visible = viewModel.state.isLoading) {
                    LoadingIndicator(
                        modifier = Modifier.fillMaxSize(0.25f),
                        color = colors.tintColor
                    )
                }
            }
        }
    }
}