package com.example.cipher.data.di.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import com.example.cipher.LocalUserProto
import com.example.cipher.data.StorageKeys.JWT_TOKEN_PREFERENCES
import com.example.cipher.data.StorageKeys.LOCAL_USER_PROTO_FILE
import com.example.cipher.data.di.JwtTokenPreference
import com.example.cipher.data.di.LocalModule
import com.example.cipher.data.di.LocalUserStore
import com.example.cipher.data.di.NetworkModule
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

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class AuthLocalModule {

    @Provides
    @Singleton
    fun provideJwtTokenManager(@JwtTokenPreference dataStore: DataStore<Preferences>): JwtTokenManager {
        return JwtTokenStorage(dataStore = dataStore)
    }

    @Provides
    @Singleton
    fun provideLocalUserManager(@LocalUserStore dataStore: DataStore<LocalUserProto>): LocalUserManager {
        return LocalUserStorage(dataStore = dataStore)
    }

    @Provides
    @Singleton
    @JwtTokenPreference
    fun provideJwtDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return LocalModule.createPreferenceDataStore(context, JWT_TOKEN_PREFERENCES)
    }

    @Provides
    @Singleton
    @LocalUserStore
    fun provideUserDataStore(@ApplicationContext context: Context): DataStore<LocalUserProto> {
        return createDataStore(context, LocalUserSerializer, LOCAL_USER_PROTO_FILE)
    }

    private fun <T> createDataStore(context: Context, serializer: Serializer<T>, fileName: String): DataStore<T> {
        return DataStoreFactory.create (
            serializer = serializer
        ) {
            context.dataStoreFile(fileName)
        }
    }
}