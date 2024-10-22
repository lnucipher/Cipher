package com.example.cipher.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cipher.domain.models.auth.AuthResult
import com.example.cipher.ui.common.navigation.AuthNavGraph
import com.example.cipher.ui.common.navigation.GlobalNavScreens
import com.example.cipher.ui.screens.auth.composable.AuthAlertDialog
import com.example.cipher.ui.screens.auth.composable.rememberImeState
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.images

@Composable
fun AuthScreen(
    mainNavController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val isImeVisible by rememberImeState()

    LaunchedEffect(viewModel) {
        viewModel.authResult.collect { result ->
            if (result is AuthResult.Authorized) {
                mainNavController.navigate(GlobalNavScreens.HomeScreen) {
                    popUpTo(GlobalNavScreens.AuthScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.primaryBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val maxUpperSectionRatio = remember {
            mutableFloatStateOf(0.15f)
        }
        val animatedUpperSectionRatio by animateFloatAsState(
            targetValue = if (isImeVisible) 0.0f else maxUpperSectionRatio.floatValue,
            label = ""
        )

       AnimatedVisibility(visible = !isImeVisible) {
           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .fillMaxHeight(animatedUpperSectionRatio),
               contentAlignment = Alignment.Center
           ) {
               Image(
                   painter = images.logo,
                   contentDescription = null,
                   contentScale = ContentScale.Crop,
                   modifier = Modifier
                       .fillMaxWidth(if (maxUpperSectionRatio.floatValue >= 0.25f) 0.3f else 0.0f)
               )
           }
       }


        AuthNavGraph(
            maxUpperSectionRatio = maxUpperSectionRatio,
            isImeVisible = isImeVisible,
            authViewModel = viewModel
        )
    }

    if (viewModel.state.showErrorDialog) {
       AuthAlertDialog {
           viewModel.state = viewModel.state.copy(showErrorDialog = false)
       }
    }

    if (viewModel.state.isLoading) {
        CircularProgressIndicator()
    }
}
