package com.example.cipher.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController()
) {
    Scaffold (
        bottomBar = {
            HomeNavigationBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HomeNavGraph(navController = navController)
        }
    }
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
                .fillMaxHeight(0.1f)
                .shadow(12.dp, shape = RoundedCornerShape(0.dp)),
            containerColor = colors.secondaryBackground
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
