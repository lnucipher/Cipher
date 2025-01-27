package com.example.cipher.data.local.db.contact.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cipher.data.local.db.contact.model.ContactEntity
import com.example.cipher.domain.models.user.Status
import java.time.LocalDateTime

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ContactEntity>)

    @Query("SELECT * FROM contacts")
    fun pagingSource(): PagingSource<Int, ContactEntity>

    @Query("UPDATE contacts SET status = :newStatus, lastSeen = :newLastSeen WHERE id = :contactId")
    suspend fun updateStatusAndLastSeenById(contactId: String, newStatus: Status, newLastSeen: LocalDateTime)

    @Query("delete from contacts where id in (:idList)")
    fun deleteAllByIds(idList: List<String>)

    @Query("DELETE FROM contacts")
    suspend fun clearAll()
}