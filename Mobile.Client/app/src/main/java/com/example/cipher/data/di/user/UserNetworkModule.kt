package com.example.cipher.data.di.user

import com.example.cipher.data.di.NetworkModule
import com.example.cipher.data.remote.repository.FakeUserRepositoryImpl
import com.example.cipher.domain.repository.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class UserNetworkModule {

    @Provides
    @Singleton
    fun provideUserRepository (): UserRepository {
        return FakeUserRepositoryImpl()
    }

}