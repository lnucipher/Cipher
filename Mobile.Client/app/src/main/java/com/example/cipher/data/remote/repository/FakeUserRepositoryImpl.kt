package com.example.cipher.data.remote.repository

import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.user.UserRepository
import java.sql.Timestamp
import kotlin.random.Random

class FakeUserRepositoryImpl: UserRepository {
    private val mockUserList = List(8) { index ->
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

    override suspend fun getUsersByUsername(username: String): List<User> {
        return mockUserList
    }
}