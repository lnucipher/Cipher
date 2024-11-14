package com.example.cipher.ui.screens.home.chats.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.ImageLoader
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.shapes
import com.example.cipher.ui.common.theme.CipherTheme.typography

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    isImeVisible: Boolean,
    keyboardController: SoftwareKeyboardController?,
    searchResult: State<List<Pair<User, Boolean>>>,
    onClick: (user: User, isContact: Boolean) -> Unit,
    imageLoader: ImageLoader,
    onSearch: (String) -> Unit,
    onCancel: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    val searchFieldHeightRatio by animateFloatAsState(
        targetValue = if (!isImeVisible) 0.75f else 0.15f,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )

    val cancelButtonHeightRatio by animateFloatAsState(
        targetValue = if (!isImeVisible) 0.001f else 0.35f,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )

    DisposableEffect(isImeVisible) {
        if (!isImeVisible) {
            text = ""
            onCancel()
        }
        onDispose { }
    }

    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    keyboardController?.hide()
                }
            }
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        BasicTextField(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .background(
                    colors.secondaryBackground,
                    shape = shapes.componentShape
                )
                .padding(4.dp),
            value = text,
            onValueChange = {
                text = it
            },
            singleLine = true,
            textStyle = typography.body.copy(color = colors.primaryText),
            cursorBrush = SolidColor(colors.primaryText),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(text)
                }
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(searchFieldHeightRatio)
                        .wrapContentWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(
                        onClick = { onSearch(text) },
                        modifier = Modifier
                            .padding(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = colors.secondaryText
                        )
                    }
                }
                Box (
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = "Search",
                            style = typography.body,
                            color = colors.secondaryText
                        )
                    }
                    it.invoke()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(cancelButtonHeightRatio)
                .wrapContentWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = {
                    keyboardController?.hide()
                }
            ) {
                Text(
                    text = "Cancel",
                    style = typography.body,
                    color = colors.secondaryText
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
    SearchList(
        modifier = Modifier.zIndex(1f),
        isImeVisible = isImeVisible,
        onClick = onClick,
        imageLoader = imageLoader,
        users = searchResult.value
    )
}

