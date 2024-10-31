package com.example.cipher.domain.repository.message

import androidx.paging.PagingData
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.models.message.MessageRequest
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessageList(senderId: String, receiverId: String): Flow<PagingData<Message>>
    suspend fun sendMessage(request: MessageRequest)
    suspend fun saveMessage(message: Message)
}