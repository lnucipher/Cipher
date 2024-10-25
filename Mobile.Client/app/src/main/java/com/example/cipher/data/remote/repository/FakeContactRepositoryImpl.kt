package com.example.cipher.data.remote.repository

import androidx.paging.PagingData
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.contact.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.sql.Timestamp
import kotlin.random.Random

class FakeContactRepositoryImpl : ContactRepository  {
    private val mockUserList = List(15) { index ->
        User(
            id = (index + 1).toString(),
            username = "user${index + 1}",
            name = "User ${index + 1}",
            bio = "Bio of User ${index + 1}",
            birthDate = "199${index % 10}-${Random.nextInt(1, 28)}-${Random.nextInt(1, 28)}",
            avatarUrl = "https://pixabay.com/photos/upside-down-wine-barrel-art-funny-2404825/",
            status = if (Random.nextBoolean()) Status.ONLINE else Status.OFFLINE,
            lastSeen = Timestamp.valueOf("2024-10-${Random.nextInt(20, 26)} 12:00:00")
        )
    }


    override fun getContactList(): Flow<PagingData<User>> {
        val pagingData = PagingData.from(mockUserList)
        return flowOf(pagingData)
    }
}