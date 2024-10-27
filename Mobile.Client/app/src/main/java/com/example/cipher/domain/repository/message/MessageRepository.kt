package com.example.cipher.domain.repository.message

import androidx.paging.PagingData
import com.example.cipher.domain.models.message.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessageList(): Flow<PagingData<Message>>
}