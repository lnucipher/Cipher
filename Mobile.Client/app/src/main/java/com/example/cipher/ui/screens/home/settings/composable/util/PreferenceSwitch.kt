package com.example.cipher.ui.screens.home.settings.composable.util

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.example.cipher.ui.common.theme.CipherTheme.colors

@Composable
fun PreferenceSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        modifier = Modifier.scale(0.7f),
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors().copy(
            checkedThumbColor = colors.secondaryBackground,
            uncheckedThumbColor = colors.tintColor,
            uncheckedBorderColor = colors.tintColor,
            uncheckedTrackColor = colors.primaryBackground,
            checkedTrackColor = colors.tintColor,
            checkedBorderColor = colors.tintColor
        )
    )
}