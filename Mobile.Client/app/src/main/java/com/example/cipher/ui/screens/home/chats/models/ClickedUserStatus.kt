package com.example.cipher.ui.screens.home.chats.models

import com.example.cipher.domain.models.user.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

data class ClickedUserStatus(
    val userId: String? = null,
    val status: Status? = null,
    val lastSeen: LocalDateTime? = null
)

internal object ClickedUserStatusManager {
    private val _clickedUserStatus = MutableStateFlow(ClickedUserStatus())
    val clickedUserStatus: StateFlow<ClickedUserStatus> get() = _clickedUserStatus

    fun updateClickedUserStatus(userId: String?, status: Status?, lastSeen: LocalDateTime?) {
        _clickedUserStatus.value = ClickedUserStatus(userId, status, lastSeen)
    }

    fun updateClickedUserStatusOnDefault() {
        _clickedUserStatus.value = ClickedUserStatus(null, null, null)
    }
}
