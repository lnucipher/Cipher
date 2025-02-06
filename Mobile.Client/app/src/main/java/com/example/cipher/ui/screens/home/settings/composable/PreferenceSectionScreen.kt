package com.example.cipher.ui.screens.home.settings.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography

@Composable
fun PreferencesSectionScreen(
    title: String,
    items: List<@Composable () -> Unit>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.5.dp)
            .background(colors.secondaryBackground)
            .padding(vertical = 14.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            color = colors.tintColor,
            style = typography.toolbar.copy(
                fontSize = 18.sp
            )
        )
        items.forEachIndexed { index, item ->
            item()
            if (index < items.size - 1) {
                HorizontalDivider(thickness = 0.5.dp, color = colors.tintColor)
            }
        }
    }
}
