package com.example.cipher.ui.screens.home.profile.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography

@Composable
fun ProfileTopBar(
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.tintColor.copy(alpha = 1f),
                        colors.tintColor.copy(alpha = 0.9f),
                        colors.tintColor.copy(alpha = 0.8f),
                        colors.tintColor.copy(alpha = 0.7f),
                        colors.tintColor.copy(alpha = 0.6f),
                        colors.tintColor.copy(alpha = 0.5f),
                        colors.tintColor.copy(alpha = 0.4f),
                        colors.tintColor.copy(alpha = 0.3f),
                        colors.tintColor.copy(alpha = 0.2f),
                        colors.tintColor.copy(alpha = 0.1f),
                        colors.primaryBackground.copy(alpha = 0.1f),
                        colors.primaryBackground.copy(alpha = 0.2f),
                        colors.primaryBackground.copy(alpha = 0.3f),
                        colors.primaryBackground.copy(alpha = 0.4f),
                    )
                )
            )
            .padding(16.dp)
            .zIndex(0f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                border = BorderStroke(1.dp, color = colors.primaryText),
                onClick = { onEditClick() }
            ) {
                Text(
                    text = "Edit",
                    color = colors.primaryText,
                    style = typography.body
                )
//                Icon(
//                    painter = painterResource(R.drawable.edit_icon),
//                    contentDescription = "Edit",
//                    tint = colors.primaryText
//                )
            }
        }
    }
}