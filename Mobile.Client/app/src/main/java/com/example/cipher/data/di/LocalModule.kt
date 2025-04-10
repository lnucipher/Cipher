package com.example.cipher.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.cipher.data.local.db.AppDatabase
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

    companion object {
        fun createPreferenceDataStore(context: Context, name: String): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create (
                corruptionHandler = ReplaceFileCorruptionHandler (
                    produceNewData = { emptyPreferences() }
                ),
                produceFile = { context.preferencesDataStoreFile(name) }
            )
        }
    }
}