package com.example.cipher.ui.common.navigation

import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.User
import kotlinx.serialization.Serializable

@Serializable
sealed class NavScreen

@Serializable
sealed class GlobalNavScreens : NavScreen() {
    @Serializable
    data object AuthScreen : GlobalNavScreens()

    @Serializable
    data object SplashScreen : GlobalNavScreens()

    @Serializable
    data object HomeScreen : GlobalNavScreens()
}

@Serializable
sealed class AuthNavScreens : NavScreen() {
    @Serializable
    data object LoginScreen : AuthNavScreens()

    @Serializable
    data object SignUpScreen : AuthNavScreens()

    @Serializable
    data object AdditionalInfoScreen : AuthNavScreens()
}

@Serializable
sealed class HomeNavScreens : NavScreen() {
    @Serializable
    data object ChatsScreen : HomeNavScreens()

    @Serializable
    data object SettingsScreen : HomeNavScreens()
}

@Serializable
sealed class ChatNavScreens : NavScreen() {
    @Serializable
    data class PersonalChatScreen(val contact: User, val localUser: LocalUser) : ChatNavScreens()

    @Serializable
    data class UserProfileScreen(val user: User) : ChatNavScreens()
}
