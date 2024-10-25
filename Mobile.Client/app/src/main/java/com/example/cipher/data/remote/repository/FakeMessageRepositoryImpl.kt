package com.example.cipher.data.remote.repository

import androidx.paging.PagingData
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.repository.message.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.sql.Timestamp

class FakeMessageRepositoryImpl : MessageRepository {

    private val currentTime = System.currentTimeMillis()
    private val mockMessages =
        listOf(
            Message("1", "user1", "user2", "Hello", Timestamp(currentTime - 100000)),
            Message("2", "user2", "user1", "I'm good, thanks! And you?", Timestamp(currentTime - 90000)),
            Message("3", "user1", "user2", "Just working on some stuff.", Timestamp(currentTime - 80000)),
            Message("4", "user2", "user1", "Sounds interesting! What kind of stuff?", Timestamp(currentTime - 70000)),
            Message("5", "user1", "user2", "Just some coding projects.", Timestamp(currentTime - 60000)),
            Message("6", "user2", "user1", "Nice! I love coding too.", Timestamp(currentTime - 50000)),
            Message("7", "user1", "user2", "What languages do you work with?", Timestamp(currentTime - 40000)),
            Message("8", "user2", "user1", "Mainly Kotlin and Java.", Timestamp(currentTime - 30000)),
            Message("9", "user1", "user2", "Awesome! I'm focusing on Android development.", Timestamp(currentTime - 20000)),
            Message("10", "user2", "user1", "That's great! Let's work on a project together sometime.That's great! Let's work on a project together sometime", Timestamp(currentTime - 10000)),
            Message("11", "user2", "user1", "That's great! Let's work on a project together sometime.That's great! Let's work on a project together sometime", Timestamp(currentTime - 10000)),
            Message("12", "user2", "user1", "That's great! Let's work on a project together sometime.That's great! Let's work on a project together sometime", Timestamp(currentTime - 10000)),
            Message("13", "user2", "user1", "That's great! Let's work on a project together sometime.That's great! Let's work on a project together sometime", Timestamp(currentTime - 10000))
        )

    override fun getMessageList(): Flow<PagingData<Message>> {
        val pagingData = PagingData.from(mockMessages)
        return flowOf(pagingData)
    }
}