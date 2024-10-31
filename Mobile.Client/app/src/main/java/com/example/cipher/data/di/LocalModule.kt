package com.example.cipher.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.cipher.LocalUserProto
import com.example.cipher.data.StorageKeys.JWT_TOKEN_PREFERENCES
import com.example.cipher.data.StorageKeys.LOCAL_USER_PROTO_FILE
import com.example.cipher.data.local.db.AppDatabase
import com.example.cipher.data.local.storage.JwtTokenStorage
import com.example.cipher.data.local.storage.LocalUserStorage
import com.example.cipher.data.local.storage.models.LocalUserSerializer
import com.example.cipher.domain.repository.auth.JwtTokenManager
import com.example.cipher.domain.repository.user.LocalUserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app.dp"
        ).fallbackToDestructiveMigration().build()
    }

}