package com.example.cipher.data.remote.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.example.cipher.data.local.db.AppDatabase
import com.example.cipher.data.mappers.toContactEntity
import com.example.cipher.data.mappers.toUser
import com.example.cipher.data.remote.api.ContactApi
import com.example.cipher.data.remote.api.dto.AddContactRequestDto
import com.example.cipher.data.remote.api.mediator.ContactRemoteMediator
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.contact.ContactRepository
import com.example.cipher.ui.screens.home.chats.models.ClickedUserStatusManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val contactApi: ContactApi
): ContactRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getContactList(userId: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 1,
                enablePlaceholders = true
            ),
            remoteMediator = ContactRemoteMediator(
                database = database,
                contactApi = contactApi,
                userId = userId
            ),
            pagingSourceFactory = {
                database.contactDao.pagingSource()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toUser() }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateContactStatus(userId: String, status: Status, lastSeen: LocalDateTime) {
        database.contactDao.updateStatusAndLastSeenById(userId, status, lastSeen)
        if (ClickedUserStatusManager.clickedUserStatus.value.userId == userId) {
            ClickedUserStatusManager
                .updateClickedUserStatus(
                    userId = userId,
                    status = status,
                    lastSeen = lastSeen
                )
        }
    }

    override suspend fun addContact(primaryUserId: String, user: User) {
        contactApi.addContact(AddContactRequestDto(
            primaryUserId = primaryUserId,
            secondaryUserId = user.id
        ))
        database.contactDao.insertAll(listOf(user.toContactEntity()))
    }

    override suspend fun deleteContact(primaryUserId: String, userIds: Set<String>) {
        userIds.forEach {
            contactApi.deleteContact(
                primaryUserId = primaryUserId,
                secondaryUserId = it
            )
        }
        withContext(Dispatchers.IO) {
            database.withTransaction {
                database.contactDao.deleteAllByIds(userIds.toList())
                try {
                    database.unreadNotificationsDao.deleteAllBySenderIds(userIds.toList())
                } catch (_: Exception) { }
            }
        }
    }
}