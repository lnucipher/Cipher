//package com.example.cipher.ui.screens.home.composable
//
//import androidx.compose.foundation.pager.PagerState
//import androidx.compose.material3.Icon
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.NavigationBarItemDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import com.example.cipher.ui.common.navigation.HomeNavScreens
//import com.example.cipher.ui.common.theme.CipherTheme.colors
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.launch
//
//@Composable
//fun HomeNavigationBar(
//    modifier: Modifier = Modifier,
//    pagerState: PagerState,
//    scope: CoroutineScope
//) {
//    NavigationBar(
//        modifier = modifier,
//        containerColor = colors.primaryBackground
//    ) {
//        HomeNavScreens.entries.forEachIndexed { index, screen ->
//            NavigationBarItem(
//                selected = pagerState.currentPage == index,
//                icon = {
//                    Icon(
//                        painter = painterResource(screen.iconResource),
//                        contentDescription = null
//                    )
//                },
//                label = {
//                    Text(text = screen.title)
//                },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = colors.primaryText,
//                    selectedTextColor = colors.primaryText,
//                    indicatorColor = Color.Transparent,
//                    unselectedIconColor = colors.secondaryText,
//                    unselectedTextColor = colors.secondaryText
//                ),
//                onClick = {
//                    scope.launch {
//                        pagerState.animateScrollToPage(index)
//                    }
//                }
//            )
//        }
//    }
//}
