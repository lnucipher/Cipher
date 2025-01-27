package com.example.cipher.ui.screens.home.chats.models

data class ChatsMultiSelectionState (
    val isMultiSelectionEnabled: Boolean = false,
    val itemsSelected: Set<String> = emptySet()
)