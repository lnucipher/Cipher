package com.example.cipher.ui.screens.home.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.repository.message.GetMessageList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PersonalChatViewModel @Inject constructor(
    getMessageList: GetMessageList
): ViewModel() {

    val messagePagingDataFlow: Flow<PagingData<Message>> = getMessageList()
        .cachedIn(viewModelScope)

}