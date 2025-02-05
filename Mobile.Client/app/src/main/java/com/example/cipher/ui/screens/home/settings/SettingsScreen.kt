package com.example.cipher.ui.screens.home.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.R
import com.example.cipher.data.mappers.toUser
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.ui.common.theme.CipherTheme
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.screens.home.composable.HomeTopAppBar
import com.example.cipher.ui.screens.home.composable.drawer.model.NavigationDrawerState
import com.example.cipher.ui.screens.home.composable.drawer.model.opposite
import com.example.cipher.ui.screens.home.profile.composable.AccountBannerScreen
import com.example.cipher.ui.screens.home.profile.composable.AccountInfoScreen
import com.example.cipher.ui.screens.home.settings.composable.PreferenceChatsScreen
import com.example.cipher.ui.screens.home.settings.composable.PreferencesCPrivacySection
import com.example.cipher.ui.screens.home.settings.composable.PreferencesColorThemeSection
import com.example.cipher.ui.screens.home.settings.composable.PreferencesLanguageSection
import com.example.cipher.ui.screens.home.settings.composable.PreferencesNotificationSection
import com.example.cipher.ui.screens.home.settings.composable.util.SelectionDialog
import com.example.cipher.ui.screens.home.settings.composable.util.SetupPhotoButton
import com.example.cipher.ui.screens.home.settings.model.DialogType
import com.example.cipher.ui.screens.home.settings.model.NotificationSound
import com.example.cipher.ui.screens.home.settings.model.NotificationVibration

@Composable
fun SettingsScreen(
    user: LocalUser,
    drawerState: NavigationDrawerState,
    onDrawerToggle: (NavigationDrawerState) -> Unit,
//    viewModel: SettingsViewModel = hiltViewModel()
) {
//    LaunchedEffect(user) {
//        viewModel.setLocalUser(localUser = user)
//    }
    Scaffold (
        topBar = {
            HomeTopAppBar(
                title = "Settings",
                drawerState = drawerState,
                containerColor = colors.tintColor,
                contentColor = colors.tertiaryText,
                onDrawerToggle = { onDrawerToggle(drawerState.opposite()) }
            )
        }
    ) { innerPadding ->
        var showDialogFor: DialogType? by remember { mutableStateOf(null) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AccountBannerScreen(user.toUser())

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item(key = "photoButton") {
                    SetupPhotoButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.5.dp)
                            .background(colors.secondaryBackground)
                            .padding(vertical = 14.dp, horizontal = 24.dp),
                        title = "Setup profile photo",
                        icon = painterResource(R.drawable.calendar_month_icon),
                        onPhotoSelected = {}
                    )
                }
                item(key = "accountInfo") {
                    AccountInfoScreen(
                        user = user.toUser(),
                        isLocalUser = true,
                        onChangeClick = {}
                    )
                }
                item(key = "preferencesNotification") { PreferencesNotificationSection(showDialogFor = { showDialogFor = it }) }
                item(key = "preferenceChats") { PreferenceChatsScreen() }
                item(key = "preferencesColorTheme") { PreferencesColorThemeSection() }
                item(key = "preferencesLanguage") { PreferencesLanguageSection(showDialogFor = { showDialogFor = it }) }
                item(key = "preferencesCPrivacy") { PreferencesCPrivacySection() }
            }


            when (showDialogFor) {
                DialogType.SOUND -> {
                    SelectionDialog(
                        title = "Select Notification Sound",
                        options = NotificationSound.entries.toTypedArray(),
                        selectedOption = NotificationSound.NONE,
                        onOptionSelected = {
                            showDialogFor = null
                        },
                        onDismiss = { showDialogFor = null }
                    )
                }

                DialogType.VIBRATION -> {
                    SelectionDialog(
                        title = "Select Notification Vibration",
                        options = NotificationVibration.entries.toTypedArray(),
                        selectedOption = NotificationVibration.NONE,
                        onOptionSelected = {
                            showDialogFor = null
                        },
                        onDismiss = { showDialogFor = null }
                    )
                }

                DialogType.LANGUAGE -> {
                    SelectionDialog(
                        title = "Select Language",
                        options = NotificationVibration.entries.toTypedArray(),
                        selectedOption = NotificationVibration.NONE,
                        onOptionSelected = {
                            showDialogFor = null
                        },
                        onDismiss = { showDialogFor = null }
                    )
                }

                else -> {}
            }

        }
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