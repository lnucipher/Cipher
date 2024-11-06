package com.example.cipher.data.remote.api.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import coil.network.HttpException
import com.example.cipher.data.local.db.AppDatabase
import com.example.cipher.data.local.db.message.model.MessageEntity
import com.example.cipher.data.local.db.message.model.MessageRemoteKeyEntity
import com.example.cipher.data.mappers.toMessageEntity
import com.example.cipher.data.remote.api.MessageApi
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import okio.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator @Inject constructor(
    private val database: AppDatabase,
    private val messageApi: MessageApi,
    val senderId: String,
    val receiverId: String
): RemoteMediator<Int, MessageEntity>() {

    companion object {
        private const val MESSAGE_STARTING_PAGE_INDEX = 1
        private const val MESSAGE_KEY_ID = "message_key"
    }

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
    ): MediatorResult {
        return try {
            val page = getPageForLoadType(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

            val responseData = when (
                val apiResponse = messageApi.getMessages(
                    senderId = senderId,
                    receiverId = receiverId,
                    page = page,
                    pageSize = state.config.pageSize
                )
            ) {
                is ApiResponse.Success -> apiResponse.data
                is ApiResponse.Failure -> throw IOException(apiResponse.message())
            }

            val items = responseData.items
            val endOfPaginationReached = items.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.messageRemoteKeyDao.clearRemoteKey(MESSAGE_KEY_ID)
                    database.messageDao.clearAll()
                }
                database.messageRemoteKeyDao.insert(
                    MessageRemoteKeyEntity(
                    id = MESSAGE_KEY_ID,
                    hasPreviousPage =  responseData.hasPreviousPage,
                    hasNextPage =  responseData.hasNextPage,
                    pageNumber = page
                )
                )
                database.messageDao.insertAll(items.map {
                    it.toMessageEntity()
                })
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private fun getPageForLoadType(loadType: LoadType): Int? {
        val remoteKey = database.messageRemoteKeyDao.getRemoteKey(MESSAGE_KEY_ID)
        return when (loadType) {
            LoadType.REFRESH -> MESSAGE_STARTING_PAGE_INDEX
            LoadType.PREPEND -> if (remoteKey?.hasPreviousPage == true) remoteKey.pageNumber - 1 else null
            LoadType.APPEND -> if (remoteKey?.hasNextPage == true) remoteKey.pageNumber + 1 else null
        }
    }
}