package com.example.cipher.data.local.db.contact.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cipher.data.local.db.contact.model.ContactEntity
import com.example.cipher.data.local.db.message.model.MessageEntity

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ContactEntity>)

    @Query("SELECT * FROM contacts")
    fun getContacts(): PagingSource<Int, ContactEntity>

    @Query("DELETE FROM contacts")
    fun clearAll()
}