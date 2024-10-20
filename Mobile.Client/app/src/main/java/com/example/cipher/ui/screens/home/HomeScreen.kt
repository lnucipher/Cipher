package com.example.cipher.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cipher.ui.common.navigation.BottomBarScreens
import com.example.cipher.ui.common.navigation.HomeNavGraph
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController()
) {
    Scaffold (
        topBar = { HomeTopAppBar(navController) },
        bottomBar = {
            HomeNavigationBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(innerPadding)
        ) {
            HomeNavGraph(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val title = when(currentRoute) {
        BottomBarScreens.Chats.route -> BottomBarScreens.Chats.title
        BottomBarScreens.Profile.route -> BottomBarScreens.Profile.title
        BottomBarScreens.Settings.route -> BottomBarScreens.Settings.title
        else -> {"Cipher"}
    }

    val navigationIcon = when(currentRoute) {
        BottomBarScreens.Chats.route -> null
        BottomBarScreens.Profile.route -> Icons.AutoMirrored.Filled.ArrowBack
        BottomBarScreens.Settings.route -> Icons.AutoMirrored.Filled.ArrowBack
        else -> {Icons.AutoMirrored.Filled.ArrowBack}
    }

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
            navigationIcon?.let { icon ->
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.primaryText
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = colors.primaryText
                )
            }
        }
    )
}

@Composable
fun HomeNavigationBar(
    navController: NavController
) {
    val screens = listOf(
        BottomBarScreens.Profile, BottomBarScreens.Chats, BottomBarScreens.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any{ it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar(
            modifier = Modifier
                .fillMaxHeight(0.11f)
                .shadow(
                    elevation = 10.dp,
                ),
            containerColor = colors.primaryBackground
        ) {
            screens.forEach { screen ->
                NavigationBarItem(
                    label = { Text(text = screen.title) },
                    icon = {
                        Icon(
                            painter = painterResource(screen.iconResource),
                            contentDescription = null
                        )
                    },
                    selected = currentDestination?.hierarchy?.any {
                        it.route == screen.route
                    } == true,
                    colors = NavigationBarItemColors(
                        selectedIconColor = colors.primaryText,
                        selectedTextColor = colors.primaryText,
                        selectedIndicatorColor = Color.Transparent,
                        unselectedIconColor = colors.secondaryText,
                        unselectedTextColor = colors.secondaryText,
                        disabledIconColor = colors.secondaryText,
                        disabledTextColor = colors.secondaryText
                    ),
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
