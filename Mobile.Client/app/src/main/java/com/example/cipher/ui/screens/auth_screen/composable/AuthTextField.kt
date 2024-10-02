package com.example.cipher.ui.screens.auth_screen.composable

import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cipher.ui.theme.CipherTheme
import com.example.cipher.ui.theme.CipherTheme.colors
import com.example.cipher.ui.theme.CipherTheme.shapes
import com.example.cipher.ui.theme.CipherTheme.typography


@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    label: String,
    height: Dp = 36.dp,
    maxSymbols: Int = 20,
    singleLine: Boolean = true,
    placeholder: String? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    val visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None

    Column(modifier = modifier) {

        Text(
            text = label,
            color = colors.primaryText,
            style = typography.body
        )
        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            value = text,
            onValueChange = {
                if (it.length <= maxSymbols) {
                    if (!singleLine && it.length > (maxSymbols/2)) it + "\n"

                    text = it
                    onValueChange(it)
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            textStyle = typography.caption.copy(color = colors.secondaryText),
            cursorBrush = SolidColor(colors.secondaryText),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = colors.tintColor,
                        shape = shapes.cornersStyle
                    )
                    .heightIn(height),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box (
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = shapes.padding.mediumPadding),
                    contentAlignment = Alignment.CenterStart
                ) {

                    if (text.isEmpty()) {
                        Text(
                            text = placeholder ?: "",
                            color = colors.secondaryText,
                            style = typography.caption
                        )
                    }

                    it.invoke()
                }

                if (isPassword) {
                    val icon = if (passwordVisible) Icons.Filled.CheckCircle else Icons.Filled.Lock
                    IconButton(
                        modifier = Modifier
                            .size(height)
                            .padding(end = 8.dp),
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = colors.tintColor
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AuthTextFieldPreview() {
    CipherTheme (darkTheme = true) {
        AuthTextField(
            modifier = Modifier
                .padding(16.dp),
            label = "Password",
            isPassword = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions()
        ) {

        }
    }
}


