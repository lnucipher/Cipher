package com.example.cipher.ui.common.navigation

import com.example.cipher.R

sealed class GlobalNavScreens (val route: String) {
    data object AuthScreen: GlobalNavScreens(route = "auth_screen")
    data object SplashScreen: GlobalNavScreens(route = "splash_screen")
    data object HomeScreen: GlobalNavScreens(route = "home_screen")
}

sealed class AuthNavScreens (val route: String) {
    data object LoginScreen: AuthNavScreens(route = "login_screen")
    data object SignUpScreen: AuthNavScreens(route = "signup_screen")
    data object AdditionalInfoScreen: AuthNavScreens(route = "additional_info_screen")
}

sealed class BottomBarScreens(val title: String, val route: String, val iconResource: Int) {
    data object Chats : BottomBarScreens(title = "Chats", route = "chats_screen", iconResource = R.drawable.chats_icon)
    data object Profile : BottomBarScreens(title = "Profile", route = "profile_screen", iconResource = R.drawable.account_circle_icon)
    data object Settings : BottomBarScreens(title = "Settings", route = "settings_screen", iconResource = R.drawable.settings_icon)
}