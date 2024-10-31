package com.example.cipher.ui.screens.home.chat.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cipher.R
import com.example.cipher.ui.common.theme.CipherTheme.colors

@Composable
fun ChatBox(
    onValueSend: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(colors.primaryBackground)
            .padding(4.dp)
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row {
            Spacer(modifier = Modifier.fillMaxWidth(0.05f))

            ChatTextField (
                modifier = Modifier
                    .weight(1f)
            ) {
                text = it
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.15f)
                    .padding(bottom = 6.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onValueSend(text)
                        }
                    },
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.send_icon),
                            contentDescription = null,
                            tint = colors.tintColor
                        )
                    }
                )

            }
        }
    }
}