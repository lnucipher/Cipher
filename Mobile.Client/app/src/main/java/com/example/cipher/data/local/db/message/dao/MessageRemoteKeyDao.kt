package com.example.cipher.data.local.db.message.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cipher.data.local.db.message.model.MessageRemoteKeyEntity

@Dao
interface MessageRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: MessageRemoteKeyEntity)

    @Query("SELECT * FROM message_remote_key WHERE id = :id")
    fun getRemoteKey(id: String): MessageRemoteKeyEntity?

    @Query("DELETE FROM message_remote_key WHERE id = :id")
    suspend fun clearRemoteKey(id: String)
}