package com.example.cipher.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.cipher.data.storage.JwtTokenStorage
import com.example.cipher.domain.repository.auth.JwtTokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    @Singleton
    @JwtTokenPreference
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return createDataStore(context, DiDataStoreKeys.JWT_TOKEN_PREFERENCES)
    }

    private fun createDataStore(context: Context, name: String): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create (
            corruptionHandler = ReplaceFileCorruptionHandler (
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.preferencesDataStoreFile(name) }
        )
    }

    @Provides
    @Singleton
    fun provideJwtTokenManager(@JwtTokenPreference dataStore: DataStore<Preferences>): JwtTokenManager {
        return JwtTokenStorage(dataStore = dataStore)
    }

}