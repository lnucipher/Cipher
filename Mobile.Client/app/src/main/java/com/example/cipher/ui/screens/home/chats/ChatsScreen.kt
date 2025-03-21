package com.example.cipher.ui.screens.home.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.ui.common.composable.LoadingIndicator
import com.example.cipher.ui.common.navigation.ChatNavScreens
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.screens.auth.composable.rememberImeState
import com.example.cipher.ui.screens.home.chat.composable.EmptyChatState
import com.example.cipher.ui.screens.home.chats.composable.ChatsItem
import com.example.cipher.ui.screens.home.chats.composable.ChatsTopAppBar
import com.example.cipher.ui.screens.home.chats.composable.search.SearchField
import com.example.cipher.ui.screens.home.composable.drawer.model.NavigationDrawerState
import com.example.cipher.ui.screens.home.composable.drawer.model.opposite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatsScreen(
    localUser: LocalUser,
    navController: NavHostController,
    drawerState: NavigationDrawerState,
    onDrawerToggle: (NavigationDrawerState) -> Unit,
    viewModel: ChatsViewModel = hiltViewModel()
) {
    val contacts = viewModel
        .contactPagingDataFlow.collectAsLazyPagingItems()

    val keyboardController = LocalSoftwareKeyboardController.current
    val isImeVisible by rememberImeState()

    val lazyColumnListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val shouldShowEmptyState = remember { mutableStateOf(false) }

    val multiSelectionState by remember { viewModel.multiSelectionState }

    LaunchedEffect(localUser) {
        viewModel.setLocalUser(localUser = localUser)
    }

    LaunchedEffect(contacts) {
        shouldShowEmptyState.value = false
        delay(300)
        shouldShowEmptyState.value = true
    }

    Scaffold(
        topBar = {
            ChatsTopAppBar(
                multiSectionEnabled = multiSelectionState.isMultiSelectionEnabled,
                itemsSelected = multiSelectionState.itemsSelected,
                onDelete = { ids ->
                    viewModel.deleteContacts(ids)
                    viewModel.disableMultiSelection()
                },
                onCancel = {
                    viewModel.disableMultiSelection()
                },
                onMute = {
                    viewModel.disableMultiSelection()
                },
                drawerState = drawerState,
                onDrawerToggle = { onDrawerToggle(drawerState.opposite()) }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(innerPadding)
                .padding(12.dp)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchField(
                modifier = Modifier,
                isImeVisible = isImeVisible,
                keyboardController = keyboardController,
                searchResult = viewModel.searchResults.collectAsStateWithLifecycle(),
                imageLoader = viewModel.imageLoader,
                onSearch = { searchedUsername ->
                    if (searchedUsername.isNotEmpty()) {
                        viewModel.searchUsers(searchedUsername)
                    }
                },
                onCancel = {
                    viewModel.clearSearchResults()
                },
                onClick = { user, isContact ->
                    if (!isContact) {
                        viewModel.addContact(user)
                    }
                    keyboardController?.hide()
                    navController.navigate(
                        ChatNavScreens.PersonalChatScreen(
                            contact = user,
                            localUser = localUser
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                contacts.loadState.refresh is LoadState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier.fillMaxSize(0.25f),
                        color = colors.tintColor
                    )
                }
                contacts.loadState.refresh is LoadState.NotLoading && contacts.itemCount == 0 -> {
                    if (shouldShowEmptyState.value) {
                        EmptyChatState()
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(0f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = lazyColumnListState
                    ) {
                        coroutineScope.launch {
                            val isAtTop = lazyColumnListState.firstVisibleItemIndex == 0 &&
                                    lazyColumnListState.firstVisibleItemScrollOffset == 0
                            if (isAtTop) {
                                delay(100)
                                lazyColumnListState.animateScrollToItem(0)
                            }
                        }
                        items(
                            count = contacts.itemCount,
                            key = contacts.itemKey { it.id }
                        ) { index ->
                            val contact = contacts[index]
                            if (contact != null) {
                                ChatsItem(
                                    contact = contact,
                                    isSelected = multiSelectionState.itemsSelected.contains(contact.id),
                                    imageLoader = viewModel.imageLoader,
                                    isMuted = viewModel.getIsMutedBySenderId(contact.id),
                                    unreadNotificationCount = viewModel.countNotificationsBySenderId(contact.id),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    if (!multiSelectionState.isMultiSelectionEnabled) {
                                                        viewModel.enableMultiSelection()
                                                        viewModel.toggleItemSelection(contact.id)
                                                    } else {
                                                        viewModel.toggleItemSelection(contact.id)
                                                    }
                                                },
                                                onTap = {
                                                    if (multiSelectionState.isMultiSelectionEnabled) {
                                                        viewModel.toggleItemSelection(contact.id)
                                                    } else {
                                                        navController.navigate(
                                                            ChatNavScreens.PersonalChatScreen(
                                                                contact = contact,
                                                                localUser = localUser
                                                            )
                                                        )
                                                        viewModel.deleteAllUnreadNotificationBySenderId(contact.id)
                                                    }
                                                }
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
}


