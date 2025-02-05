package com.example.cipher.ui.screens.home.settings.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography

@Composable
fun PreferencesSectionItem(
    title: String,
    description: String? = null,
    action: @Composable () -> Unit,
    isRow: Boolean = true
) {
    if (isRow) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    color = colors.primaryText,
                    style = typography.body.copy(
                        fontSize = 16.sp
                    )
                )
                description?.let {
                    Text(
                        text = it,
                        color = colors.secondaryText,
                        style = typography.caption.copy(
                            fontSize = 14.sp
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            action()
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                color = colors.primaryText,
                style = typography.body.copy(
                    fontSize = 16.sp
                )
            )
            action()
        }
    }
}
