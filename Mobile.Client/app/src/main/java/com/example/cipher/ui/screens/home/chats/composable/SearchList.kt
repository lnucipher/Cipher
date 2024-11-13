package com.example.cipher.ui.screens.home.chats.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography

@Composable
fun SearchList(
    modifier: Modifier = Modifier,
    isImeVisible: Boolean,
    onClick: (user: User, isContact: Boolean) -> Unit,
    users: List<Pair<User, Boolean>>
) {
    AnimatedVisibility(
        visible = isImeVisible,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(users) { data ->
                SearchListItem(
                    user = data.first,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick(data.first, data.second)
                        }
                )
            }
            item {
                if (users.isEmpty()) {
                    Text(
                        text = "No results found",
                        style = typography.body,
                        color = colors.secondaryText,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}