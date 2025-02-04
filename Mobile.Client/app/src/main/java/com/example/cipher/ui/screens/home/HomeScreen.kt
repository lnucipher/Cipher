package com.example.cipher.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.cipher.ui.common.theme.CipherTheme.colors
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.cipher.ui.common.navigation.HomeNavGraph
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.cipher.ui.common.navigation.HomeNavScreens
import com.example.cipher.ui.screens.home.composable.drawer.NavigationDrawer
import com.example.cipher.ui.screens.home.composable.drawer.model.CustomDrawerState
import com.example.cipher.ui.screens.home.composable.drawer.model.DrawerItem
import com.example.cipher.ui.screens.home.composable.drawer.model.isOpened
import kotlin.math.roundToInt

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val localUser by viewModel.localUser.collectAsStateWithLifecycle()

    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    var selectedNavigationItem by remember { mutableStateOf(DrawerItem.Chats) }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 4.5).dp } }

    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp,
        animationSpec = tween(300),
        label = "Animated Offset"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f,
        animationSpec = tween(300),
        label = "Animated Scale"
    )
    val animatedCorner by animateDpAsState(
        targetValue = if (drawerState.isOpened()) 12.dp else 0.dp,
        label = "Animated Corner"
    )

    BackHandler(enabled = drawerState.isOpened()) {
        drawerState = CustomDrawerState.Closed
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.secondaryBackground),
        contentAlignment = Alignment.CenterStart
    ) {
        NavigationDrawer (
            onNavigationItemClick = { item ->
                selectedNavigationItem = item
                drawerState = CustomDrawerState.Closed
                when (item) {
                    DrawerItem.Chats -> navController.navigate(HomeNavScreens.ChatsScreen)
                    DrawerItem.Settings -> navController.navigate(HomeNavScreens.SettingsScreen)
                    else -> Unit
                }
            },
            onLogoutItemClick = { viewModel.logout(context) },
            localUser = localUser,
            imageLoader = viewModel.imageLoader,
            selectedNavigationItem = selectedNavigationItem
        )
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .offset(x = animatedOffset)
                    .fillMaxHeight(animatedScale)
                    .clip(RoundedCornerShape(animatedCorner))
                    .shadow(if (drawerState.isOpened()) 10.dp else 0.dp)
            ) {
                HomeNavGraph(
                    navController = navController,
                    localUser = localUser,
                    drawerState = drawerState,
                    onDrawerClick = { drawerState = it }
                )
            }
        }
    }
}

