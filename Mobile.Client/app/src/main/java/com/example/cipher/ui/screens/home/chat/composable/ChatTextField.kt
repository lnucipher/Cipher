package com.example.cipher.ui.screens.home.chat.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.shapes
import com.example.cipher.ui.common.theme.CipherTheme.typography

@Composable
fun ChatTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    Row (
        modifier = modifier
            .background(
                colors.secondaryBackground,
                shape = shapes.componentShape
            )
            .heightIn(42.dp)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxSize(),
            value = text,
            onValueChange = {
                text = it
                onValueChange(text)
            },
            maxLines = 5,
            singleLine = false,
            textStyle = typography.body.copy(color = colors.primaryText),
            cursorBrush = SolidColor(colors.primaryText),
        ) {

            Box (
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {

                if (text.isEmpty()) {
                    Text(
                        text = "Message",
                        style = typography.body,
                        color = colors.secondaryText
                    )
                }
                it.invoke()
            }
        }
    }
}
