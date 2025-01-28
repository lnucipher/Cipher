package com.example.cipher.data.local.db.notification.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cipher.data.local.db.notification.model.UnreadNotificationsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UnreadNotificationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: UnreadNotificationsEntity)

    @Query("SELECT * FROM unread_notifications")
    fun getAllNotifications(): Flow<List<UnreadNotificationsEntity>>

    @Query("DELETE FROM unread_notifications WHERE senderId in (:senderIds)")
    suspend fun deleteAllBySenderIds(senderIds: List<String>)

    @Query("UPDATE unread_notifications SET isMuted = :isMuted WHERE senderId = :senderId")
    suspend fun updateIsMutedBySenderId(senderId: String, isMuted: Boolean)
}