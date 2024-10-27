package com.example.cipher.domain.repository.contact

import androidx.paging.PagingData
import com.example.cipher.domain.models.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetContactList @Inject constructor(
    private val contactRepository: ContactRepository
) {
    operator fun invoke(): Flow<PagingData<User>> = contactRepository.getContactList()
        .flowOn(Dispatchers.IO)
}