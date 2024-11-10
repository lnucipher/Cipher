package com.example.cipher.ui.screens.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@Composable
fun PersonalChatScreen(
    viewModel: PersonalChatViewModel = hiltViewModel(),
    navController: NavHostController,
    localUser: LocalUser,
    contact: User
) {
    LaunchedEffect(Unit) {
        viewModel.setUserIds(localUser.id, contact.id)
    }

    val messages = viewModel.messagePagingDataFlow.collectAsLazyPagingItems()
    val lazyColumnListState = rememberLazyListState()
    val corroutineScope = rememberCoroutineScope()

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
            ChatBox(onValueSend = { text ->
                viewModel.sendMessage(text)
            })
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
                messages.itemCount == 0 && messages.loadState.refresh is LoadState.NotLoading -> {
                    EmptyChatState()
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        reverseLayout = true,
                        state = lazyColumnListState,
                        flingBehavior = rememberSnapFlingBehavior(lazyColumnListState)
                    ) {
                        corroutineScope.launch {
                            if(messages.itemCount > 15){
                                lazyColumnListState.animateScrollToItem(0)
                            }
                        }
                        items(
                            count = messages.itemCount,
                            key = messages.itemKey{ it.id }
                        ) { index ->
                            val message = messages[index]
                            val previousMessage = if (index > 0) messages[index - 1] else null
                            val nextMessage = if (index < messages.itemCount - 1) messages[index + 1] else null

                            val isFirstInSequence = previousMessage?.senderId != message?.senderId
                            val isMiddleInSequence = previousMessage?.senderId == message?.senderId && nextMessage?.senderId == message?.senderId
                            val isLastInSequence = nextMessage?.senderId != message?.senderId

                            if (message != null) {
                                val isLocalUserMessage = message.senderId == localUser.id

                                Spacer(modifier = Modifier.height(if (!isFirstInSequence) 4.dp else 12.dp))

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
                                            .weight(5f, false)
                                            .padding(horizontal = if (isFirstInSequence) 0.dp else 8.dp),
                                        message = message,
                                        isLocalUser = isLocalUserMessage,
                                        isFirstInSequence = isFirstInSequence,
                                        isMiddleInSequence = isMiddleInSequence,
                                        isLastInSequence = isLastInSequence
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
