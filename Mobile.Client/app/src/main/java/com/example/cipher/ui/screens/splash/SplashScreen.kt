package com.example.cipher.ui.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import com.example.cipher.ui.common.navigation.GlobalNavScreens
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.images

@Composable
fun SplashScreen(mainNavController: NavController) {
    val scale = remember { androidx.compose.animation.core.Animatable(1f) }
    val translationXAnim = remember { androidx.compose.animation.core.Animatable(-200f) }

    LaunchedEffect(key1 = true) {
        translationXAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = { OvershootInterpolator(2f).getInterpolation(it) }
            )
        )
        scale.animateTo(
            targetValue = 1.8f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = { OvershootInterpolator(2f).getInterpolation(it) }
            )
        )

        mainNavController.navigate(GlobalNavScreens.HomeScreen.route) {
            popUpTo(GlobalNavScreens.SplashScreen.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
    ) {
        Image(
            painter = images.logo,
            contentDescription = "Logo",
            modifier = Modifier
                .scale(scale.value)
                .graphicsLayer {
                    translationX = translationXAnim.value
                }
        )
    }
}

