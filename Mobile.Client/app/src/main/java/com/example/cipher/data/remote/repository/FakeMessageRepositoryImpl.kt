package com.example.cipher.data.remote.repository

import androidx.paging.PagingData
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.repository.message.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

class FakeMessageRepositoryImpl : MessageRepository {

    private val currentTime = System.currentTimeMillis()
    fun createLocalDateTime(offset: Long): LocalDateTime {
        return LocalDateTime.now(ZoneOffset.UTC).minusSeconds(offset)
    }
    private val mockMessages =
        listOf(
            Message("1", "user1", "user2", "Hello", createLocalDateTime(100)),
            Message("2", "user2", "user1", "I'm good, thanks! And you?", createLocalDateTime(90)),
            Message("3", "user1", "user2", "Just working on some stuff.", createLocalDateTime(80)),
            Message("4", "user2", "user1", "Sounds interesting! What kind of stuff?", createLocalDateTime(70)),
            Message("5", "user1", "user2", "Just some coding projects.", createLocalDateTime(60)),
            Message("6", "user2", "user1", "Nice! I love coding too.", createLocalDateTime(50)),
            Message("7", "user1", "user2", "What languages do you work with?", createLocalDateTime(40)),
            Message("8", "user2", "user1", "Mainly Kotlin and Java.", createLocalDateTime(30)),
            Message("9", "user1", "user2", "Awesome! I'm focusing on Android development.", createLocalDateTime(20)),
            Message("10", "user2", "user1", "That's great! Let's work on a project together sometime.", createLocalDateTime(10)),
            Message("11", "user2", "user1", "That's great! Let's work on a project together sometime.", createLocalDateTime(10)),
            Message("12", "user2", "user1", "That's great! Let's work on a project together sometime.", createLocalDateTime(10)),
            Message("13", "user2", "user1", "That's great! Let's work on a project together sometime.", createLocalDateTime(10))
        )

    override fun getMessageList(senderId: String, receiverId: String): Flow<PagingData<Message>> {
        val pagingData = PagingData.from(mockMessages)
        return flowOf(pagingData)
    }
}