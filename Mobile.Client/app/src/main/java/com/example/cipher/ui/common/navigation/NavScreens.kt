package com.example.cipher.ui.common.navigation


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

sealed class HomeNavScreens (val route: String) {
    data object Chats: HomeNavScreens(route = "chats_screen")
}