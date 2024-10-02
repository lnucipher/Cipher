package com.example.cipher.ui.screens.auth_screen.login_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cipher.ui.screens.auth_screen.AuthScreen
import com.example.cipher.ui.screens.auth_screen.composable.AuthTextField
import com.example.cipher.ui.theme.CipherTheme
import com.example.cipher.ui.theme.CipherTheme.colors
import com.example.cipher.ui.theme.CipherTheme.shapes
import com.example.cipher.ui.theme.CipherTheme.typography

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    isImeVisible:Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(colors.secondaryBackground)
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.fillMaxSize(0.05f))
        Text(
            text = "Log in",
            color = colors.primaryText,
            style = typography.heading
        )
        Spacer(modifier = Modifier.fillMaxSize(0.1f))

        AuthTextField(
            modifier = Modifier
                .padding(bottom = 24.dp),
            label = "Login",
            height = 42.dp,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions()
        ) {

        }

        AuthTextField(
            modifier = Modifier
                .padding(bottom = 24.dp),
            label = "Password",
            isPassword = true,
            height = 42.dp,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions()
        ) {

        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp).padding(bottom = 12.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = colors.primaryText,
                containerColor = colors.tintColor
            ),
            shape = shapes.cornersStyle
        ) {
            Text(
                text = "Login",
                style = typography.toolbar
            )
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = colors.primaryText,
                containerColor = Color.Transparent
            ),
            shape = shapes.cornersStyle,
            border = BorderStroke(2.dp, colors.tintColor)

        ) {
            Text(
                text = "Create new",
                style = typography.toolbar
            )
        }

        if (isImeVisible) {
            Spacer(modifier = Modifier.fillMaxSize(0.3f))
        } else {
            Spacer(modifier = Modifier.fillMaxSize(0f))
        }

    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    CipherTheme (darkTheme = true) {
        AuthScreen()
    }
}