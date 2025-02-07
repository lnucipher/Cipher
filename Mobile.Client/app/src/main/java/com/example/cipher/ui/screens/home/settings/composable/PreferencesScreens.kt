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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.R
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.models.settings.Settings
import com.example.cipher.domain.models.settings.Theme
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.screens.home.chat.composable.MessageItem
import com.example.cipher.ui.screens.home.settings.composable.util.DialogType
import com.example.cipher.ui.screens.home.settings.composable.util.NotificationOptionText
import com.example.cipher.ui.screens.home.settings.composable.util.PreferenceSlider
import com.example.cipher.ui.screens.home.settings.composable.util.PreferenceSwitch
import com.example.cipher.ui.screens.home.settings.composable.util.SetupPhotoButton
import java.time.LocalDateTime

@Composable
fun PreferencesNotificationSection(
    settings: Settings,
    showDialogFor: (DialogType) -> Unit,
    notificationEnabledChange: (Boolean) -> Unit
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
                            checked = settings.isNotificationEnabled,
                            onCheckedChange = notificationEnabledChange
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
                            text = settings.notificationSound.label,
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
                            text = settings.notificationVibration.label,
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
fun PreferenceChatsScreen(
    settings: Settings,
    messageFontSizeChanged: (Int) -> Unit,
    messageCornerSizeChanged: (Int) -> Unit
) {
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
                    title = "Message font size",
                    isRow = false,
                    action = {
                        PreferenceSlider(
                            currentValue = settings.messageFontSize,
                            minValue = 12,
                            maxValue = 20,
                            onValueChange = messageFontSizeChanged
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
                            currentValue = settings.messageCornersSize,
                            minValue = 0,
                            maxValue = 20,
                            onValueChange = messageCornerSizeChanged
                        )
                    }
                )
            }
        )
    )
}

@Composable
fun PreferencesColorThemeSection(
    settings: Settings,
    themeChanged: (Theme) -> Unit,
    wallpaperChanged: (String) -> Unit,
    isDarkThemeChanged: (Boolean) -> Unit
) {
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
                    Theme.entries.forEach { theme ->
                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .clickable { themeChanged(theme) }
                                .border(2.dp, colors.tintColor, RoundedCornerShape(12.dp)),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            ThemePickerItem(
                                tintColor = theme.lightPalette.tintColor,
                                primaryBackgroundColor = theme.lightPalette.primaryBackground,
                                secondaryBackgroundColor = theme.lightPalette.secondaryBackground,
                                primaryTextColor = theme.lightPalette.primaryText
                            )
                            ThemePickerItem(
                                tintColor = theme.darkPalette.tintColor,
                                primaryBackgroundColor = theme.darkPalette.primaryBackground,
                                secondaryBackgroundColor = theme.darkPalette.secondaryBackground,
                                primaryTextColor = theme.darkPalette.primaryText
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
                    onPhotoSelected = wallpaperChanged
                )
            },
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clickable {
                            isDarkThemeChanged(settings.darkTheme?.not() ?: false)
                        },
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
                        text = if (settings.darkTheme == true) "Switch to day mode"
                        else "Switch to night mode",
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
    settings: Settings,
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
                        text = settings.language.label,
                        onClick = { showDialogFor(DialogType.LANGUAGE) }
                    )
                }
            )
        }
    )
}

@Composable
fun PreferencesCPrivacySection(
    onLogout: () -> Unit,
    onPasswordChange: () -> Unit
) {
    PreferencesSectionScreen(
        title = "Privacy settings",
        items = listOf(
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clickable {
                            onPasswordChange()
                        },
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
                        text = "Change password",
                        color = colors.tintColor,
                        style = typography.body.copy(fontSize = 18.sp)
                    )
                }
            },
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clickable {
                            onLogout()
                        },
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
                        text = "Logout",
                        color = colors.tintColor,
                        style = typography.body.copy(fontSize = 18.sp)
                    )
                }
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
