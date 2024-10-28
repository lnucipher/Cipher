package com.example.cipher.ui.screens.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.ui.common.composable.LoadingIndicator
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.screens.home.chat.composable.ChatBox
import com.example.cipher.ui.screens.home.chat.composable.EmptyChatState
import com.example.cipher.ui.screens.home.chat.composable.MessageItem
import com.example.cipher.ui.screens.home.chat.composable.PersonalChatTopAppBar

@Composable
fun PersonalChatScreen(
    viewModel: PersonalChatViewModel = hiltViewModel(),
    navController: NavHostController,
    localUser: LocalUser,
    contact: User
) {
    val messages = viewModel
        .getMessagePagingDataFlow(
            senderId = localUser.id,
            receiverId = contact.id
        ).collectAsLazyPagingItems()

    LaunchedEffect(key1 = messages.loadState) {
        if (messages.loadState.refresh is LoadState.Error) {
            // TODO implement error handling
        }
    }

    Scaffold (
        modifier = Modifier.imePadding(),
        topBar = {
            PersonalChatTopAppBar(
                navController = navController,
                chatCoUser = contact
            )
        },
        bottomBar = {
            ChatBox()
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(colors.secondaryBackground)
                .padding(innerPadding)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            when {
                messages.loadState.refresh is LoadState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier.fillMaxSize(0.25f),
                        color = colors.tintColor
                    )
                }
                messages.itemCount == 0 -> {
                    EmptyChatState()
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        reverseLayout = true
                    ) {
                        items(
                            count = messages.itemCount,
                            key = messages.itemKey{ it.id }
                        ) { index ->
                            val message = messages[index]
                            if (message != null) {
                                val isLocalUserMessage = message.senderId == localUser.id
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = if (isLocalUserMessage) Arrangement.End else Arrangement.Start
                                ) {
                                    if (isLocalUserMessage) {
                                        Spacer(modifier = Modifier.weight(1.0f))
                                    }

                                    MessageItem(
                                        modifier = Modifier
                                            .weight(5f, false),
                                        message = message,
                                        isLocalUser = isLocalUserMessage
                                    )

                                    if (!isLocalUserMessage) {
                                        Spacer(modifier = Modifier.weight(1.0f))
                                    }
                                }
                            }
                        }
                        item {
                            if (messages.loadState.append is LoadState.Loading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(42.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LoadingIndicator(
                                        modifier = Modifier
                                            .requiredSize(42.dp),
                                        color = colors.tintColor
                                    )
                                }
                            }

                        }
                    }
                }
            }

        }
    }
}
