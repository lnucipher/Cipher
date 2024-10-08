package com.example.cipher.ui.screens.auth_screen.register_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cipher.ui.screens.auth_screen.AuthViewModel
import com.example.cipher.ui.screens.auth_screen.composable.AuthTextField
import com.example.cipher.ui.screens.auth_screen.composable.ImagePickerButton
import com.example.cipher.ui.screens.auth_screen.utils.AuthValidation
import com.example.cipher.ui.screens.auth_screen.register_screen.models.SignUpUiEvent
import com.example.cipher.ui.theme.CipherTheme.colors
import com.example.cipher.ui.theme.CipherTheme.shapes
import com.example.cipher.ui.theme.CipherTheme.typography

@Composable
fun AdditionalInfoScreen(
    isImeVisible: Boolean,
    navController: NavHostController,
    maxUpperSectionRatio: MutableState<Float>,
    viewModel: SignUpViewModel = hiltViewModel(),
    authViewModel: AuthViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.setAuthViewModel(authViewModel)
    }

    val focusManager = LocalFocusManager.current
    maxUpperSectionRatio.value = 0.15f


    Column(
        modifier = Modifier
            .fillMaxSize()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(topStart = 39.dp, topEnd = 39.dp)
            )
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(colors.secondaryBackground)
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedVisibility(visible = !isImeVisible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.30f),
                contentAlignment = Alignment.Center
            ) {
                ImagePickerButton()
            }
        }
        AnimatedVisibility(visible = isImeVisible, enter = fadeIn(tween(500)), exit = fadeOut(tween(100))) {
            Text(
                text = "Additional Info",
                color = colors.primaryText,
                style = typography.heading
            )
        }

        Spacer(modifier = Modifier.fillMaxSize(0.1f))

        AuthTextField(
            modifier = Modifier
                .padding(bottom = 16.dp),
            label = "Displayed name",
            height = 42.dp,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            validation = AuthValidation.EmptyValidation,
            isValid = viewModel.validationState.isNameValid
        ) {
            viewModel.onEvent(SignUpUiEvent.NamedChanged(it))
        }

        AuthTextField(
            modifier = Modifier
                .padding(bottom = 16.dp),
            label = "Bio",
            height = 42.dp,
            maxSymbols = 100,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.clearFocus()
                }
            ),
            isValid = viewModel.validationState.isBioValid
        ) {
            viewModel.onEvent(SignUpUiEvent.BioChanged(it))
        }

        AuthTextField(
            modifier = Modifier
                .padding(bottom = 32.dp),
            label = "Birth Date",
            height = 42.dp,
            isDate = true,
            readOnly = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(),
            validation = AuthValidation.BirthDateValidation,
            isValid = viewModel.validationState.isBirthDateValid
        ) {
            viewModel.onEvent(SignUpUiEvent.BirthDateChanged(it))
        }

        Button(
            onClick = {
                viewModel.onEvent(SignUpUiEvent.SignUp)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 12.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = colors.tertiaryText,
                containerColor = colors.tintColor
            ),
            shape = shapes.componentShape
        ) {
            Text(
                text = "Create",
                style = typography.toolbar
            )
        }

        Button(
            onClick = { navController.popBackStack() },
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
                style = typography.toolbar
            )
        }

    }
}
