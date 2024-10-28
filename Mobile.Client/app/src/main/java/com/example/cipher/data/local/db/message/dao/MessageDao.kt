package com.example.cipher.data.local.db.message.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cipher.data.local.db.message.model.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MessageEntity>)

    @Query("SELECT * FROM messages WHERE (senderId = :senderId AND receiverId = :receiverId) OR (senderId = :receiverId AND receiverId = :senderId)")
    fun getMessagesByUsers(senderId: String, receiverId: String): PagingSource<Int, MessageEntity>

    @Query("DELETE FROM messages WHERE (senderId = :senderId AND receiverId = :receiverId) OR (senderId = :receiverId AND receiverId = :senderId)")
    fun clearMessagesByUsers(senderId: String, receiverId: String)
}