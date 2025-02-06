package com.example.cipher.ui.screens.home.profile.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.screens.home.settings.composable.util.PreferenceSwitch

@Composable
fun AccountInfoScreen(
    user: User,
    isLocalUser: Boolean,
    isNotificationEnabled: Boolean? = null,
    notificationStateChange: ((Boolean) -> Unit)? = null,
    onChangeClick: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.5.dp)
            .background(colors.secondaryBackground)
            .padding(vertical = 14.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Info",
            color = colors.tintColor,
            style = typography.toolbar.copy(
                fontSize = 18.sp
            )
        )

        AccountInfoItem(
            modifier = Modifier.clickable { onChangeClick?.invoke() },
            title = "Username",
            info = "@${user.username}"
        )

        HorizontalDivider(thickness = 0.5.dp, color = colors.tintColor)
        AccountInfoItem(
            modifier = Modifier.clickable { onChangeClick?.invoke() },
            title = "Birthday",
            info = user.birthDate.ifEmpty { "Not embedded" }
        )

        HorizontalDivider(thickness = 0.5.dp, color = colors.tintColor)
        AccountInfoItem(
            modifier = Modifier.clickable { onChangeClick?.invoke() },
            title = "Bio",
            info = user.bio.ifEmpty { "Not embedded" }
        )

        if (!isLocalUser) {
            HorizontalDivider(thickness = 0.5.dp, color = colors.tintColor)
            Row {
                AccountInfoItem(
                    title = if (isNotificationEnabled == true) "Enabled"
                    else "Disabled",
                    info = "Notifications"
                )
                Spacer(modifier = Modifier.weight(1f))
                PreferenceSwitch(
                    checked = isNotificationEnabled == true,
                    onCheckedChange = { notificationStateChange?.invoke(it) }
                )
            }
        }
    }
}

@Composable
fun AccountInfoItem(
    modifier: Modifier = Modifier,
    title: String,
    info: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = info,
            color = colors.primaryText,
            style = typography.body.copy(
                fontSize = 18.sp
            )
        )
        Text(
            text = title,
            color = colors.secondaryText,
            style = typography.caption.copy(
                fontSize = 16.sp
            )
        )
    }
}

