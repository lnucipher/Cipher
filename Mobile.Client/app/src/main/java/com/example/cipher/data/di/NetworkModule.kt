package com.example.cipher.data.di

import com.example.cipher.data.repository.FakeAuthRepository
import com.example.cipher.domain.repository.auth.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideFakeAuthRepository(): AuthRepository {
        return FakeAuthRepository()
    }

}