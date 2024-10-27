package com.example.cipher.ui.common.navigation

import com.example.cipher.R
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.User
import kotlinx.serialization.Serializable

@Serializable
sealed class GlobalNavScreens {
    @Serializable
    data object AuthScreen: GlobalNavScreens()

    @Serializable
    data object SplashScreen: GlobalNavScreens()

    @Serializable
    data object HomeScreen: GlobalNavScreens()
}

@Serializable
sealed class AuthNavScreens {
    @Serializable
    data object LoginScreen: AuthNavScreens()

    @Serializable
    data object SignUpScreen: AuthNavScreens()

    @Serializable
    data object AdditionalInfoScreen: AuthNavScreens()
}

@Serializable
sealed class HomeNavScreens {
    @Serializable
    data object ChatsScreen: AuthNavScreens()

    @Serializable
    data object ProfileScreen: AuthNavScreens()

    @Serializable
    data object SettingsScreen: AuthNavScreens()
}

@Serializable
sealed class BottomBarScreens<T>(val title: String, val iconResource: Int, val route: T) {
    @Serializable
    data object Chats : BottomBarScreens<HomeNavScreens.ChatsScreen>(title = "Chats", route = HomeNavScreens.ChatsScreen, iconResource = R.drawable.chats_icon)

    @Serializable
    data object Profile : BottomBarScreens<HomeNavScreens.ProfileScreen>(title = "Profile", route = HomeNavScreens.ProfileScreen, iconResource = R.drawable.account_circle_icon)

    @Serializable
    data object Settings : BottomBarScreens<HomeNavScreens.SettingsScreen>(title = "Settings", route = HomeNavScreens.SettingsScreen, iconResource = R.drawable.settings_icon)
}

@Serializable
sealed class ChatNavScreens {
    @Serializable
    data class PersonalChatScreen(val contact: User, val localUser: LocalUser) : ChatNavScreens()
}
