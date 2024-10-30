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
    private val mockUserList = List(16) { index ->
        User(
            id = (index + 1).toString(),
            username = "user${index + 1}",
            name = "User ${index + 1}",
            bio = "Bio of User ${index + 1}",
            birthDate = "199${index % 10}-${Random.nextInt(1, 28)}-${Random.nextInt(1, 28)}",
            avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
            status = if (Random.nextBoolean()) Status.ONLINE else Status.OFFLINE,
            lastSeen = Timestamp.valueOf("2024-10-${Random.nextInt(20, 26)} 12:00:00")
        )
    }
    private val additionalUser = User(
        id = "3fa85f64-5717-4562-b3fc-2c963f66afa5",
        username = "Max",
        name = "User Max",
        bio = "Bio of User Max",
        birthDate = "1991-${Random.nextInt(1, 28)}-${Random.nextInt(1, 28)}",
        avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
        status = if (Random.nextBoolean()) Status.ONLINE else Status.OFFLINE,
        lastSeen = Timestamp.valueOf("2024-10-${Random.nextInt(20, 26)} 12:00:00")
    )

    // Combining both lists into one
    private val completeUserList = mockUserList + additionalUser


    override fun getContactList(): Flow<PagingData<User>> {

        val pagingData = PagingData.from(completeUserList)
        return flowOf(pagingData)
    }
}