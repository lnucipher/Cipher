package com.example.cipher.domain.repository.contact

import androidx.paging.PagingData
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getContactList(userId: String): Flow<PagingData<User>>
    suspend fun updateContactStatus(userId: String, status: Status)
    suspend fun addContact(primaryUserId: String, user: User)
    suspend fun deleteContact(primaryUserId: String, secondaryUserId: String)
}