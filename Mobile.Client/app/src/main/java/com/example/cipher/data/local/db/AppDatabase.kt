package com.example.cipher.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cipher.data.local.db.contact.dao.ContactDao
import com.example.cipher.data.local.db.contact.dao.ContactRemoteKeyDao
import com.example.cipher.data.local.db.contact.model.ContactEntity
import com.example.cipher.data.local.db.contact.model.ContactRemoteKeyEntity
import com.example.cipher.data.local.db.message.dao.MessageDao
import com.example.cipher.data.local.db.message.dao.MessageRemoteKeyDao
import com.example.cipher.data.local.db.message.model.MessageEntity
import com.example.cipher.data.local.db.message.model.MessageRemoteKeyEntity

@Database(
    entities =
    [MessageEntity::class, MessageRemoteKeyEntity::class,
     ContactEntity::class, ContactRemoteKeyEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val messageDao: MessageDao
    abstract val messageRemoteKeyDao: MessageRemoteKeyDao

    abstract val contactDao: ContactDao
    abstract val contactRemoteKeyDao: ContactRemoteKeyDao
}