package com.example.cipher.data.remote.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.cipher.data.local.db.AppDatabase
import com.example.cipher.data.mappers.toMessage
import com.example.cipher.data.mappers.toMessageEntity
import com.example.cipher.data.remote.api.MessageApi
import com.example.cipher.data.remote.api.mediator.MessageRemoteMediator
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.models.message.MessageRequest
import com.example.cipher.domain.repository.message.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepositoryImpl
@Inject constructor(
    private val database: AppDatabase,
    private val messageApi: MessageApi
): MessageRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMessageList(senderId: String, receiverId: String): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                initialLoadSize = 25,
                prefetchDistance = 1,
                enablePlaceholders = true
            ),
            remoteMediator = MessageRemoteMediator(
                database = database,
                messageApi = messageApi,
                senderId = senderId,
                receiverId = receiverId
            ),
            pagingSourceFactory = {
                database.messageDao.pagingSource(
                    senderId = senderId,
                    receiverId = receiverId
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toMessage() }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun saveMessage(message: Message) {
        database.messageDao.insertAll(listOf(message.toMessageEntity()))
    }

    override suspend fun sendMessage(request: MessageRequest) { messageApi.addMessage(request) }
}