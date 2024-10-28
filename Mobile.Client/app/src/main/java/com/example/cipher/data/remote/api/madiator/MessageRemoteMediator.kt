package com.example.cipher.data.remote.api.madiator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.cipher.data.local.db.AppDatabase
import com.example.cipher.data.local.db.message.model.MessageEntity
import com.example.cipher.data.local.db.message.model.MessageRemoteKeyEntity
import com.example.cipher.data.mappers.toMessageEntity
import com.example.cipher.data.remote.api.MessageApi
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator @Inject constructor(
    private val database: AppDatabase,
    private val messageApi: MessageApi,
    val senderId: String,
    val receiverId: String
): RemoteMediator<Int, MessageEntity>() {

    companion object {
        private const val REMOTE_KEY_ID = "message"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
    ): MediatorResult {
        return try {
            val pageNumber = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = database.messageRemoteKeyDao.getById(REMOTE_KEY_ID)
                    remoteKey?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val apiResponse = messageApi.getMessages(
                senderId = senderId,
                receiverId = receiverId,
                page = pageNumber,
                pageSize = state.config.pageSize
            )

            val items = apiResponse.value.items
            val hasNextPage = apiResponse.value.hasNextPage
            val nextPage = if (hasNextPage) pageNumber + 1 else null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.messageDao.clearAll()
                    database.messageRemoteKeyDao.deleteById(REMOTE_KEY_ID)
                }
                database.messageDao.insertAll(
                    items.map { it.toMessageEntity() }
                )
                database.messageRemoteKeyDao.insert(
                    MessageRemoteKeyEntity(
                        id = REMOTE_KEY_ID,
                        nextPage = nextPage
                    )
                )
            }

            MediatorResult.Success(endOfPaginationReached = !hasNextPage)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

}