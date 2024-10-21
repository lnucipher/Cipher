package com.example.cipher.ui.screens.home.composable

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cipher.ui.common.navigation.BottomBarScreens
import com.example.cipher.ui.common.theme.CipherTheme.colors

@Composable
fun HomeNavigationBar(
    navController: NavController
) {
    val screens = listOf(
        BottomBarScreens.Profile, BottomBarScreens.Chats, BottomBarScreens.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier
            .fillMaxHeight(0.11f)
            .shadow(
                elevation = 10.dp,
            ),
        containerColor = colors.primaryBackground
    ) {
        screens.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route::class.qualifiedName } == true
            NavigationBarItem(
                label = { Text(text = screen.title) },
                icon = {
                    Icon(
                        painter = painterResource(screen.iconResource),
                        contentDescription = null
                    )
                },
                selected = isSelected,
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
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}