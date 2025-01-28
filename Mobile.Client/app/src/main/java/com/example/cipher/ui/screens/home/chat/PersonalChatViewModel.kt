package com.example.cipher.ui.screens.home.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import coil.ImageLoader
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.models.message.MessageRequest
import com.example.cipher.domain.repository.message.MessageRepository
import com.example.cipher.ui.common.notification.ActiveScreenTracker
import com.example.cipher.ui.screens.home.chats.models.ClickedUserStatusManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalChatViewModel @Inject constructor(
    private val repository: MessageRepository,
    val imageLoader: ImageLoader
): ViewModel() {

    private val senderReceiverIds = MutableStateFlow<Pair<String, String>?>(null)
    var showDialog = mutableStateOf(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val messagePagingDataFlow: Flow<PagingData<Message>> = senderReceiverIds
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { ids ->
            ids.let { (senderId, receiverId) ->
                ActiveScreenTracker.setActiveChatUserId(receiverId)
                repository.getMessageList(senderId, receiverId).cachedIn(viewModelScope)
            }
        }


    fun setUserIds(senderId: String, receiverId: String) {
        senderReceiverIds.value = senderId to receiverId
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            senderReceiverIds.collect { ids ->
                ids?.let { (_, receiverId) ->
                    repository.sendMessage(MessageRequest(
                        receiverId = receiverId,
                        text = text
                    ))
                }
            }
        }
    }

    override fun onCleared() {
        ActiveScreenTracker.setActiveChatUserId(null)
        ClickedUserStatusManager.updateClickedUserStatusOnDefault()
        super.onCleared()
    }
}