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
enum class HomeNavScreens(
    val title: String,
    val iconResource: Int
) {
    ProfileScreen(
        title = "Profile",
        iconResource = R.drawable.account_circle_icon
    ),

    ChatsScreen(
        title = "Chats",
        iconResource = R.drawable.chats_icon
    ),

    SettingsScreen(
        title = "Settings",
        iconResource = R.drawable.settings_icon
    );
}

@Serializable
sealed class ChatNavScreens {
    @Serializable
    data class PersonalChatScreen(val contact: User, val localUser: LocalUser) : ChatNavScreens()
}
