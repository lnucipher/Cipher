package com.example.cipher.data.remote.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.cipher.data.local.db.AppDatabase
import com.example.cipher.data.mappers.toContactEntity
import com.example.cipher.data.mappers.toUser
import com.example.cipher.data.remote.api.ContactApi
import com.example.cipher.data.remote.api.dto.AddContactRequestDto
import com.example.cipher.data.remote.api.mediator.ContactRemoteMediator
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.contact.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
                enablePlaceholders = false
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

    override suspend fun updateContactStatus(userId: String, status: Status) {
        database.contactDao.updateStatusById(userId, status)
    }

    override suspend fun addContact(primaryUserId: String, user: User) {
        contactApi.addContact(AddContactRequestDto(
            primaryUserId = primaryUserId,
            secondaryUserId = user.id
        ))
        database.contactDao.insertAll(listOf(user.toContactEntity()))
    }

    override suspend fun deleteContact(primaryUserId: String, secondaryUserId: String) {
        contactApi.deleteContact(
            primaryUserId = primaryUserId,
            secondaryUserId = secondaryUserId
        )
        database.contactDao.deleteById(secondaryUserId)
    }

}