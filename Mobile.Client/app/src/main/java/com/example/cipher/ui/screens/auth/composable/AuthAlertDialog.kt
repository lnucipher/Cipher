package com.example.cipher.ui.screens.auth.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.cipher.ui.common.theme.CipherTheme.colors

@Composable
fun AuthAlertDialog(
    text: String,
    onDismiss: () -> Unit
) {

    AlertDialog(
                onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Authentication Failed",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primaryText
            )
        },
        text = {
            Text(
                text = text,
                fontSize = 16.sp,
                color = colors.secondaryText
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = colors.primaryText,
                    containerColor = Color.Transparent
                )
            ) {
                Text("OK")
            }
        },
        containerColor = colors.primaryBackground
        )

}

