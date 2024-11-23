package com.example.cipher.ui.screens.home.profile.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.shapes
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.screens.auth.composable.AuthTextField
import com.example.cipher.ui.screens.auth.utils.AuthValidation
import com.example.cipher.ui.screens.home.profile.ProfileViewModel
import com.example.cipher.ui.screens.home.profile.models.ProfileEditEvent

@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {
    val scrollState = rememberScrollState()
    val editState = viewModel.state.profileEditState

    Column(
        modifier = modifier
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
        ) {
            AvatarPicker(
                imageLoader = viewModel.imageLoader,
                avatarUrl = editState.avatarUrl,
                onAvatarSelected = { avatarUrl ->
                    viewModel.onEvent(ProfileEditEvent.AvatarUrlChanged(avatarUrl))
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AuthTextField(
                    modifier = Modifier
                        .padding(bottom = 4.dp),
                    label = "Username",
                    height = 42.dp,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(),
                    errorMessage = AuthValidation.EmptyValidation.errorMessage,
                    isValid = viewModel.validationState.isUsernameValid,
                    text = editState.username
                ) {
                    viewModel.onEvent(ProfileEditEvent.UsernameChanged(it))
                }
                Text(
                    text = "Enter your username",
                    style = typography.caption,
                    color = colors.primaryText,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AuthTextField(
                    modifier = Modifier
                        .padding(bottom = 4.dp),
                    label = "Displayed name",
                    height = 42.dp,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(),
                    errorMessage = AuthValidation.EmptyValidation.errorMessage,
                    isValid = viewModel.validationState.isNameValid,
                    text = editState.name
                ) {
                    viewModel.onEvent(ProfileEditEvent.NamedChanged(it))
                }
                Text(
                    text = "Enter your name and add an optional photo",
                    style = typography.caption,
                    color = colors.primaryText,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AuthTextField(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .focusable(true),
                    label = "Bio",
                    height = 42.dp,
                    maxSymbols = 100,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(),
                    errorMessage = AuthValidation.BioValidation.errorMessage,
                    isValid = viewModel.validationState.isBioValid,
                    text = editState.bio
                ) {
                    viewModel.onEvent(ProfileEditEvent.BioChanged(it))
                }
                Text(
                    text = "You can add a few lines about yourself",
                    style = typography.caption,
                    color = colors.primaryText,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AuthTextField(
                    modifier = Modifier
                        .padding(bottom = 4.dp),
                    label = "Birth Date",
                    height = 42.dp,
                    isDate = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    keyboardActions = KeyboardActions(),
                    errorMessage = AuthValidation.BirthDateValidation.errorMessage,
                    isValid = viewModel.validationState.isBirthDateValid,
                    text = editState.birthDate
                ) {
                    viewModel.onEvent(ProfileEditEvent.BirthDateChanged(it))
                }
                Text(
                    text = "You can add your birthday",
                    style = typography.caption,
                    color = colors.primaryText,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Column(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.onEvent(ProfileEditEvent.UpdateFields)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colors.tertiaryText,
                        containerColor = colors.tintColor
                    ),
                    shape = shapes.componentShape
                ) {
                    Text(
                        text = "Update",
                        style = typography.body
                    )
                }

                Button(
                    onClick = {
                        viewModel.state = viewModel.state.copy(isEditing = false)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colors.secondaryText,
                        containerColor = Color.Transparent
                    ),
                    shape = shapes.componentShape,
                    border = BorderStroke(1.5.dp, colors.tintColor)

                ) {
                    Text(
                        text = "Cancel",
                        style = typography.body
                    )
                }
            }
        }
    }
}

