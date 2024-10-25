package com.example.cipher.ui.screens.home.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.cipher.ui.common.composable.LoadingIndicator
import com.example.cipher.ui.common.navigation.ChatNavScreens
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.screens.home.chat.composable.EmptyChatState
import com.example.cipher.ui.screens.home.chats.composable.ChatsItem
import com.example.cipher.ui.screens.home.chats.composable.SearchField

@Composable
fun ChatsScreen(
    navController: NavHostController,
    viewModel: ChatsViewModel = hiltViewModel()
) {
    val contacts = viewModel
        .contactPagingDataFlow.collectAsLazyPagingItems()

    val localUser by viewModel.localUser.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = contacts.loadState) {
        if (contacts.loadState.refresh is LoadState.Error) {
            // TODO implement error handling
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchField(
            modifier = Modifier
        ) {

        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            contacts.loadState.refresh is LoadState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier.fillMaxSize(0.25f),
                    color = colors.tintColor
                )
            }
            contacts.itemCount == 0 -> {
                EmptyChatState()
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        count = contacts.itemCount,
                        key = contacts.itemKey { it.id }
                    ) { index ->
                        val contact = contacts[index]
                        if (contact != null) {
                            ChatsItem(
                                user = contact,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(
                                            ChatNavScreens.PersonalChatScreen(
                                                user = contact,
                                                localUser = localUser
                                            )
                                        )
                                    }
                            )
                        }
                    }
                    item {
                        if (contacts.loadState.append is LoadState.Loading) {
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


