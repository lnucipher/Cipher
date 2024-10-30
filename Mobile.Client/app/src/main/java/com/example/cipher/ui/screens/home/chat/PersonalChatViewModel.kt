package com.example.cipher.ui.screens.home.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cipher.data.local.db.AppDatabase
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.repository.message.GetMessageList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalChatViewModel @Inject constructor(
    private val getMessageList: GetMessageList,
    private val database: AppDatabase,
): ViewModel() {

    private val senderReceiverIds = MutableStateFlow<Pair<String, String>?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val messagePagingDataFlow: Flow<PagingData<Message>> = senderReceiverIds.flatMapLatest { ids ->
        ids?.let { (senderId, receiverId) ->
            getMessageList(senderId, receiverId).cachedIn(viewModelScope)
        } ?: flowOf(PagingData.empty())
    }


    fun setUserIds(senderId: String, receiverId: String) {
        senderReceiverIds.value = senderId to receiverId
    }


    fun clearALL() {
        viewModelScope.launch {
            database.messageDao.clearAll()
            database.messageRemoteKeyDao.clearRemoteKey("message_key")
        }
    }

}