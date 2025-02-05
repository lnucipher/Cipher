package com.example.cipher.ui.screens.home.settings.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.R
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.ui.common.theme.CipherTheme
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.screens.home.chat.composable.MessageItem
import com.example.cipher.ui.screens.home.composable.drawer.model.NavigationDrawerState
import com.example.cipher.ui.screens.home.settings.SettingsScreen
import com.example.cipher.ui.screens.home.settings.composable.util.NotificationOptionText
import com.example.cipher.ui.screens.home.settings.composable.util.PreferenceSlider
import com.example.cipher.ui.screens.home.settings.composable.util.PreferenceSwitch
import com.example.cipher.ui.screens.home.settings.composable.util.SetupPhotoButton
import com.example.cipher.ui.screens.home.settings.model.DialogType
import java.time.LocalDateTime

@Composable
fun PreferencesNotificationSection(
    showDialogFor: (DialogType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.secondaryBackground)
            .padding(vertical = 14.dp)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            color = colors.tintColor,
            style = typography.toolbar.copy(
                fontSize = 18.sp
            )
        )
    }
    PreferencesSectionScreen(
        title = "Notification",
        items = listOf(
            {
                PreferencesSectionItem(
                    title = "Turn on notifications",
                    description = "Enable/Disable all notification",
                    action = {
                        PreferenceSwitch(
                            checked = true,
                            onCheckedChange = {}
                        )
                    }
                )
            },
            {
                PreferencesSectionItem(
                    title = "Sound",
                    description = "Set sound for notification",
                    action = {
                        NotificationOptionText(
                            text = "None",
                            onClick = { showDialogFor(DialogType.SOUND) }
                        )
                    }
                )
            },
            {
                PreferencesSectionItem(
                    title = "Vibration",
                    description = "Set vibration for notification",
                    action = {
                        Text(
                            modifier = Modifier.clickable { showDialogFor(DialogType.VIBRATION) },
                            text = "None",
                            style = typography.toolbar.copy(
                                fontSize = 18.sp
                            ),
                            color = colors.tintColor
                        )
                    }
                )
            }
        )
    )
}

@Composable
fun PreferenceChatsScreen() {
    PreferencesSectionScreen(
        title = "Chat settings",
        items = listOf(
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.secondaryBackground, RoundedCornerShape(12.dp))
                        .border(1.dp, colors.tintColor, RoundedCornerShape(12.dp))
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        MessageItem(
                            message = Message(
                                id = "",
                                senderId = "",
                                receiverId = "",
                                text = "Hi from Ohio",
                                createdAt = LocalDateTime.now()
                            ),
                            isFirstInSequence = true,
                            isLocalUser = false
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        MessageItem(
                            message = Message(
                                id = "",
                                senderId = "",
                                receiverId = "",
                                text = "I'm hate Ohio",
                                createdAt = LocalDateTime.now()
                            ),
                            isFirstInSequence = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            },
            {
                PreferencesSectionItem(
                    title = "Message size",
                    isRow = false,
                    action = {
                        PreferenceSlider(
                            currentValue = 17,
                            maxValue = 25,
                            onValueChange = {  }
                        )
                    }
                )
            },
            {
                PreferencesSectionItem(
                    title = "Message corners",
                    isRow = false,
                    action = {
                        PreferenceSlider(
                            currentValue = 17,
                            maxValue = 25,
                            onValueChange = {  }
                        )
                    }
                )
            }
        )
    )
}

@Composable
fun PreferencesColorThemeSection() {
    PreferencesSectionScreen(
        title = "Color theme",
        items = listOf(
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    List(6) { it }.map {
                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .clickable {  }
                                .border(2.dp, colors.tintColor, RoundedCornerShape(12.dp)),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            ThemePickerItem(
                                tintColor = Color(0xFF3045C5),
                                primaryBackgroundColor = Color(0xFF1E1E1E),
                                secondaryBackgroundColor = Color(0xFF141414),
                                primaryTextColor = Color(0xFFFFFFFF)
                            )
                            ThemePickerItem(
                                tintColor = Color(0xFFAFBBF7),
                                primaryBackgroundColor = Color(0xFFFFFFFF),
                                secondaryBackgroundColor = Color(0xFFF8FAFF),
                                primaryTextColor = Color(0xFF000000)
                            )
                        }
                    }
                }
            },
            {
                SetupPhotoButton(
                    modifier = Modifier.padding(vertical = 2.dp),
                    title = "Change chat wallpaper",
                    icon = painterResource(R.drawable.mail_icon),
                    onPhotoSelected = {}
                )
            },
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clickable { },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(R.drawable.mail_icon),
                        contentDescription = "Icon",
                        tint = colors.tintColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = "Switch to night mode",
                        color = colors.tintColor,
                        style = typography.body.copy(fontSize = 18.sp)
                    )
                }
            }
        )
    )
}

@Composable
fun PreferencesLanguageSection(
    showDialogFor: (DialogType) -> Unit
) {
    PreferencesSectionScreen(
        title = "Language",
        items = listOf {
            PreferencesSectionItem(
                title = "Choose a language",
                description = "Program language",
                action = {
                    NotificationOptionText(
                        text = "English",
                        onClick = { showDialogFor(DialogType.LANGUAGE) }
                    )
                }
            )
        }
    )
}

@Composable
fun PreferencesCPrivacySection() {
    PreferencesSectionScreen(
        title = "Privacy settings",
        items = listOf(
            {
                SetupPhotoButton(
                    modifier = Modifier.padding(vertical = 2.dp),
                    title = "Change password",
                    icon = painterResource(R.drawable.mail_icon),
                    onPhotoSelected = {}
                )
            },
            {
                SetupPhotoButton(
                    modifier = Modifier.padding(vertical = 2.dp),
                    title = "Logout",
                    icon = painterResource(R.drawable.mail_icon),
                    onPhotoSelected = {}
                )
            }
        )
    )
}

@Composable
private fun ThemePickerItem(
    tintColor: Color,
    primaryBackgroundColor: Color,
    secondaryBackgroundColor: Color,
    primaryTextColor: Color
) {
    Column(
        modifier = Modifier
            .background(secondaryBackgroundColor, RoundedCornerShape(12.dp))
            .wrapContentWidth()
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        MessageItem(
            message = Message(
                id = "",
                senderId = "",
                receiverId = "",
                text = "1",
                createdAt = LocalDateTime.now()
            ),
            isFirstInSequence = true,
            isLocalUser = false,
            containerColor = primaryBackgroundColor,
            textColor = primaryTextColor
        )

        MessageItem(
            message = Message(
                id = "",
                senderId = "",
                receiverId = "",
                text = "1",
                createdAt = LocalDateTime.now()
            ),
            isFirstInSequence = true,
            isLocalUser = true,
            containerColor = tintColor,
            textColor = primaryTextColor
        )
    }
}

@Preview()
@Composable
fun SettingsScreenPrew() {
    CipherTheme(darkTheme = false) {
        SettingsScreen(
            user =         LocalUser(
                id = "",
                username = "maxdfsasdasd",
                name = "Max",
                birthDate = "11.11.2005",
                bio = "zxczcszxzczcxzcx",
                avatarUrl = ""
            ),
            drawerState = NavigationDrawerState.Closed,
            onDrawerToggle = {}
        )
    }
}