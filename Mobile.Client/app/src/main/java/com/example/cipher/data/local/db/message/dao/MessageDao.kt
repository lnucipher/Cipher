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

    @Query(
        """
    SELECT * FROM messages 
    WHERE (senderId = :senderId AND receiverId = :receiverId) 
       OR (senderId = :receiverId AND receiverId = :senderId) 
    ORDER BY createdAt DESC
    """
    )
    fun pagingSource(senderId: String, receiverId: String): PagingSource<Int, MessageEntity>

    @Query("DELETE FROM messages")
    suspend fun clearAll()
}