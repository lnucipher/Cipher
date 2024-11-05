package com.example.cipher.data.remote.api.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import coil.network.HttpException
import com.example.cipher.data.local.db.AppDatabase
import com.example.cipher.data.local.db.contact.model.ContactEntity
import com.example.cipher.data.local.db.contact.model.ContactRemoteKeyEntity
import com.example.cipher.data.mappers.toContactEntity
import com.example.cipher.data.remote.api.ContactApi
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ContactRemoteMediator @Inject constructor(
    private val database: AppDatabase,
    private val contactApi: ContactApi,
    private val userId: String
): RemoteMediator<Int, ContactEntity>() {

    companion object {
        private const val CONTACT_STARTING_PAGE_INDEX = 1
        private const val CONTACT_KEY_ID = "contact_key"
    }

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ContactEntity>
    ): MediatorResult {
        return try {
            val page = getPageForLoadType(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

            val apiResponse = contactApi.getContacts(
                userId = userId,
                page = page,
                pageSize = state.config.pageSize
            )

            val items = apiResponse.items
            val endOfPaginationReached = items.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.contactRemoteKeyDao.clearRemoteKey(CONTACT_KEY_ID)
                    database.contactDao.clearAll()
                }
                database.contactRemoteKeyDao.insert(
                    ContactRemoteKeyEntity(
                        id = CONTACT_KEY_ID,
                        hasPreviousPage =  apiResponse.hasPreviousPage,
                        hasNextPage =  apiResponse.hasNextPage,
                        pageNumber = page
                    )
                )
                database.contactDao.insertAll(items.map {
                    it.toContactEntity()
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
        val remoteKey = database.contactRemoteKeyDao.getRemoteKey(CONTACT_KEY_ID)
        return when (loadType) {
            LoadType.REFRESH -> CONTACT_STARTING_PAGE_INDEX
            LoadType.PREPEND -> if (remoteKey?.hasPreviousPage == true) remoteKey.pageNumber - 1 else null
            LoadType.APPEND -> if (remoteKey?.hasNextPage == true) remoteKey.pageNumber + 1 else null
        }
    }

}