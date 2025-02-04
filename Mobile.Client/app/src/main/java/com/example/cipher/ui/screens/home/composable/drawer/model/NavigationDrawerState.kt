package com.example.cipher.ui.screens.home.composable.drawer.model

enum class NavigationDrawerState {
    Opened,
    Closed
}

fun NavigationDrawerState.isOpened(): Boolean {
    return this.name == "Opened"
}

fun NavigationDrawerState.opposite(): NavigationDrawerState {
    return if (this == NavigationDrawerState.Opened) NavigationDrawerState.Closed
    else NavigationDrawerState.Opened
}