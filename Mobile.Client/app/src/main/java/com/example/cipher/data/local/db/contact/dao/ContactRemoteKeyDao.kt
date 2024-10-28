package com.example.cipher.data.local.db.contact.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cipher.data.local.db.message.model.MessageRemoteKeyEntity

@Dao
interface ContactRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MessageRemoteKeyEntity)

    @Query("SELECT * FROM contact_remote_key WHERE id = :id")
    suspend fun getById(id: String): MessageRemoteKeyEntity?

    @Query("DELETE FROM contact_remote_key WHERE id = :id")
    suspend fun deleteById(id: String)
}