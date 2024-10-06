package com.example.cipher.ui.screens.auth_screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.cipher.ui.theme.CipherTheme.colors
import com.example.cipher.ui.theme.CipherTheme.shapes
import com.example.cipher.ui.theme.CipherTheme.typography


@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    label: String,
    height: Dp = 42.dp,
    maxSymbols: Int = 20,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    val visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None

    val lineCount = if (text.length <= 25) 0 else (text.length - 1) / 25
    val inputHeight = (height + (lineCount * 18.dp))

    Column(modifier = modifier) {

        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            value = text,
            onValueChange = {
                if (it.length <= maxSymbols) {
                    text = it
                    onValueChange(it)
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = maxSymbols <= 25,
            visualTransformation = visualTransformation,
            textStyle = typography.body.copy(color = colors.primaryText),
            cursorBrush = SolidColor(colors.primaryText),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 5.dp,
                        shape = shapes.componentShape
                    )
                    .background(
                        color = colors.primaryBackground,
                        shape = shapes.componentShape
                    )
                    .heightIn(inputHeight),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box (
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {

                    if (text.isEmpty()) {
                        Text(
                            text = label,
                            color = colors.secondaryText,
                            style = typography.body
                        )
                    }

                    it.invoke()
                }

                if (isPassword) {
                    val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(
                        modifier = Modifier
                            .size(height)
                            .padding(end = 8.dp),
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = colors.secondaryText
                        )
                    }
                }
            }
        }
    }
}
