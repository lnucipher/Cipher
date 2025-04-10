package com.example.cipher.ui.screens.auth.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.cipher.ui.common.navigation.AuthNavScreens
import com.example.cipher.ui.screens.auth.AuthViewModel
import com.example.cipher.ui.screens.auth.composable.AuthTextField
import com.example.cipher.ui.screens.auth.utils.AuthValidation
import com.example.cipher.ui.screens.auth.register.models.SignUpUiEvent
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.shapes
import com.example.cipher.ui.common.theme.CipherTheme.typography
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavHostController,
    maxUpperSectionRatio: MutableState<Float>,
    viewModel: SignUpViewModel = hiltViewModel(),
    authViewModel: AuthViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.setAuthViewModel(authViewModel)
    }
    val signUpState = authViewModel.state.signUp

    val focusManager = LocalFocusManager.current
    maxUpperSectionRatio.value = 0.30f

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

        Spacer(modifier = Modifier.fillMaxSize(0.05f))
        Text(
            text = "Sign Up",
            color = colors.primaryText,
            style = typography.heading
        )
        Spacer(modifier = Modifier.fillMaxSize(0.1f))

        AuthTextField(
            modifier = Modifier
                .padding(bottom = 16.dp),
            label = "Login",
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
            errorMessage = viewModel.validationState.usernameErrorMessage,
            isValid = viewModel.validationState.isUsernameValid,
            text = signUpState.username
        ) {
            viewModel.onEvent(SignUpUiEvent.UsernameChanged(it))
        }

        AuthTextField(
            modifier = Modifier
                .padding(bottom = 16.dp),
            label = "Password",
            isPassword = true,
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
            errorMessage = AuthValidation.PasswordValidation.errorMessage,
            isValid = viewModel.validationState.isPasswordValid,
            text = signUpState.password
        ) {
            viewModel.onEvent(SignUpUiEvent.PasswordChanged(it))
        }

        AuthTextField(
            modifier = Modifier
                .padding(bottom = 32.dp),
            label = "Confirm Password",
            isPassword = true,
            height = 42.dp,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.clearFocus()
                }
            ),
            errorMessage = AuthValidation.ConfirmPasswordValidation("").errorMessage,
            isValid = viewModel.validationState.isConfirmPasswordValid,
            text = signUpState.confirmPassword
        ) {
            viewModel.onEvent(SignUpUiEvent.ConfirmPasswordChanged(it))
        }

        Button(
            onClick = {
                viewModel.viewModelScope.launch {
                    if (viewModel.validateSignUpFields()) {
                        navController.navigate(AuthNavScreens.AdditionalInfoScreen)
                    }
                }
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
                text = "Next",
                style = typography.body
            )
        }

        Button(
            onClick = {
                authViewModel.onClear()
                navController.popBackStack()
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


