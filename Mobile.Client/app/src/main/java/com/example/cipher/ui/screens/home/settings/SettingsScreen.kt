package com.example.cipher.ui.screens.home.settings

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cipher.R
import com.example.cipher.data.mappers.toUser
import com.example.cipher.domain.models.settings.Language
import com.example.cipher.domain.models.settings.NotificationSound
import com.example.cipher.domain.models.settings.NotificationVibration
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.ui.common.theme.CipherTheme.colors
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
import com.example.cipher.ui.screens.home.settings.composable.util.DialogType
import com.example.cipher.ui.screens.home.settings.composable.util.SelectionDialog
import com.example.cipher.ui.screens.home.settings.composable.util.SetupPhotoButton
import kotlinx.coroutines.delay

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SettingsScreen(
    user: LocalUser,
    drawerState: NavigationDrawerState,
    onDrawerToggle: (NavigationDrawerState) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isVisible by remember { mutableStateOf(false) }
    val lazyColumnListState = rememberLazyListState()
    var showDialogFor: DialogType? by remember { mutableStateOf(null) }
    val settings = viewModel.settings.collectAsState()

    val preferenceItems = remember {
        listOf<@Composable () -> Unit>(
            {
                SetupPhotoButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.5.dp)
                        .background(colors.secondaryBackground)
                        .padding(vertical = 14.dp, horizontal = 24.dp),
                    title = "Setup profile photo",
                    icon = painterResource(R.drawable.calendar_month_icon),
                    onPhotoSelected = {})
            },
            {
                AccountInfoScreen(
                    user = user.toUser(),
                    isLocalUser = true,
                    onChangeClick = {}
                )
            },
            { PreferencesNotificationSection(
                settings = settings.value,
                showDialogFor = { showDialogFor = it },
                notificationEnabledChange = { viewModel.updateNotificationEnabled(it) }
            )},
            { PreferenceChatsScreen(
                settings = settings.value,
                messageFontSizeChanged = { viewModel.updateMessageFontSize(it) },
                messageCornerSizeChanged = { viewModel.updateMessageCornersSize(it) }
            ) },
            { PreferencesColorThemeSection(
                settings = settings.value,
                wallpaperChanged = {  },
                isDarkThemeChanged = { viewModel.updateDarkTheme(it) },
                themeChanged = { viewModel.updateTheme(it) }
            ) },
            { PreferencesLanguageSection(
                settings = settings.value,
                showDialogFor = { showDialogFor = it }
            ) },
            { PreferencesCPrivacySection(
                onLogout = { viewModel.logout(context) },
                onPasswordChange = {}
            ) }
        )
    }

    LaunchedEffect(Unit) {
        delay(200)
        isVisible = true
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AccountBannerScreen(user.toUser())

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(500)) + slideInVertically(initialOffsetY = { -50 }, animationSpec = tween(500))
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    state = lazyColumnListState
                ) {
                    items(preferenceItems.size, key = { index -> index }) { index ->
                        preferenceItems[index]()
                    }
                }
            }

            when (showDialogFor) {
                DialogType.SOUND -> {
                    SelectionDialog(
                        title = "Select Notification Sound",
                        options = NotificationSound.entries.toTypedArray(),
                        selectedOption = viewModel.settings.value.notificationSound,
                        onOptionSelected = {
                            viewModel.updateNotificationSound(it)
                            showDialogFor = null
                        },
                        onDismiss = { showDialogFor = null }
                    )
                }

                DialogType.VIBRATION -> {
                    SelectionDialog(
                        title = "Select Notification Vibration",
                        options = NotificationVibration.entries.toTypedArray(),
                        selectedOption = viewModel.settings.value.notificationVibration,
                        onOptionSelected = {
                            viewModel.updateNotificationVibration(it)
                            showDialogFor = null
                        },
                        onDismiss = { showDialogFor = null }
                    )
                }

                DialogType.LANGUAGE -> {
                    SelectionDialog(
                        title = "Select Language",
                        options = Language.entries.toTypedArray(),
                        selectedOption = viewModel.settings.value.language,
                        onOptionSelected = {
                            viewModel.updateLanguage(it)
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
