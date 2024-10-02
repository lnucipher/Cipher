package com.example.cipher.ui.screens.auth_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cipher.R
import com.example.cipher.ui.screens.auth_screen.composable.rememberImeState
import com.example.cipher.ui.screens.auth_screen.login_screen.LoginScreen
import com.example.cipher.ui.theme.CipherTheme
import com.example.cipher.ui.theme.CipherTheme.colors


@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val isImeVisible by rememberImeState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.primaryBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val animatedUpperSectionRatio by animateFloatAsState(
            targetValue = if (isImeVisible) 0.0f else 0.35f,
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
                   painter = painterResource(R.drawable.cipher_logo),
                   contentDescription = null,
                   contentScale = ContentScale.Crop,
                   modifier = Modifier
                       .fillMaxWidth(0.3f)
               )
           }
       }
        LoginScreen(isImeVisible = isImeVisible)


    }

}


@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    CipherTheme (darkTheme = true) {
        AuthScreen()
    }
}