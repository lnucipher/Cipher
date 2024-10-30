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
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosesToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: MESSAGE_STARTING_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val apiResponse = messageApi.getMessages(
                senderId = senderId,
                receiverId = receiverId,
                page = page,
                pageSize = state.config.pageSize
            )

            val items = apiResponse.items
            val endOfPaginationReached = items.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.messageRemoteKeyDao.clearAll()
                    database.messageDao.clearAll()
                }
                val prevKey = if (page == MESSAGE_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = items.map {
                    MessageRemoteKeyEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.messageRemoteKeyDao.insertAll(keys)
                database.messageDao.insertAll(items.map {
                    it.toMessageEntity()
                })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MessageEntity>): MessageRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { message ->
                database.messageRemoteKeyDao.remoteKeysMessageId(message.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MessageEntity>): MessageRemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { message ->
                database.messageRemoteKeyDao.remoteKeysMessageId(message.id)
            }
    }

    private suspend fun getRemoteKeyClosesToCurrentPosition(
        state: PagingState<Int, MessageEntity>
    ): MessageRemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { messageId ->
                database.messageRemoteKeyDao.remoteKeysMessageId(messageId)
            }
        }
    }

}