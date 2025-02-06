package com.example.cipher.data.di.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.cipher.data.StorageKeys.SETTINGS_PREFERENCES
import com.example.cipher.data.di.LocalModule
import com.example.cipher.data.di.SettingsPreference
import com.example.cipher.data.local.storage.SettingsStorage
import com.example.cipher.domain.repository.settings.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [LocalModule::class])
@InstallIn(SingletonComponent::class)
class SettingsLocalModule {

    @Provides
    @Singleton
    fun provideJwtTokenManager(@SettingsPreference dataStore: DataStore<Preferences>): SettingsRepository {
        return SettingsStorage(dataStore = dataStore)
    }

    @Provides
    @Singleton
    @SettingsPreference
    fun provideSDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return LocalModule.createPreferenceDataStore(context, SETTINGS_PREFERENCES)
    }

}