package com.example.cipher.data.local.db.message.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cipher.data.local.db.message.model.MessageRemoteKeyEntity

@Dao
interface MessageRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<MessageRemoteKeyEntity>)

    @Query("SELECT * FROM message_remote_keys WHERE id = :id")
    suspend fun remoteKeysMessageId(id: String): MessageRemoteKeyEntity?

    @Query("DELETE FROM message_remote_keys")
    suspend fun clearAll()
}