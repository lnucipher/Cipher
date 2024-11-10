package com.example.cipher.data.local.db.contact.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cipher.data.local.db.contact.model.ContactEntity
import com.example.cipher.domain.models.user.Status

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ContactEntity>)

    @Query("SELECT * FROM contacts")
    fun pagingSource(): PagingSource<Int, ContactEntity>

    @Query("UPDATE contacts SET status = :newStatus WHERE id = :contactId")
    suspend fun updateStatusById(contactId: String, newStatus: Status)

    @Query("DELETE FROM contacts")
    suspend fun clearAll()
}