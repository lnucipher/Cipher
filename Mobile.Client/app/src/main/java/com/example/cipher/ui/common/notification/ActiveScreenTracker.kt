package com.example.cipher.ui.common.notification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ActiveScreenTracker {
    private val _activeChatUserId = MutableStateFlow<String?>(null)
    val activeChatUserId: StateFlow<String?> = _activeChatUserId

    fun setActiveChatUserId(userId: String?) {
        _activeChatUserId.value = userId
    }
}