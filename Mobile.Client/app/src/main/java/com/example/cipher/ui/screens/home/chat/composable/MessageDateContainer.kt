package com.example.cipher.ui.screens.home.chat.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.common.utils.MessagesDateFormatter
import java.time.LocalDateTime

@Composable
fun MessageDateContainer(
    modifier: Modifier = Modifier,
    date: LocalDateTime
) {
    Box(
        modifier = modifier
            .heightIn(50.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = colors.primaryBackground,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 8.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = MessagesDateFormatter.getMessageDate(date),
                style = typography.body
                    .copy(
                        fontSize = 12.sp
                    ),
                color = colors.secondaryText
            )
        }
    }
}