package com.example.cipher.data.local.db.contact.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cipher.data.local.db.contact.model.ContactRemoteKeyEntity

@Dao
interface ContactRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: ContactRemoteKeyEntity)

    @Query("SELECT * FROM contact_remote_keys WHERE id = :id")
    fun getRemoteKey(id: String): ContactRemoteKeyEntity?

    @Query("DELETE FROM contact_remote_keys WHERE id = :id")
    suspend fun clearRemoteKey(id: String)
}