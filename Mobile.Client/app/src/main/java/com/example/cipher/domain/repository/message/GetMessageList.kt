package com.example.cipher.domain.repository.message

import androidx.paging.PagingData
import com.example.cipher.domain.models.message.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMessageList @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(senderId: String, receiverId: String): Flow<PagingData<Message>> =
        messageRepository.getMessageList(
            senderId = senderId,
            receiverId = receiverId
        ).flowOn(Dispatchers.IO)
}