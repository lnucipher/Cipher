package com.example.cipher.data.di.contact

import com.example.cipher.data.di.NetworkModule
import com.example.cipher.data.remote.repository.FakeContactRepositoryImpl
import com.example.cipher.domain.repository.contact.ContactRepository
import com.example.cipher.domain.repository.contact.GetContactList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ContactNetworkModule {

    @Provides
    @Singleton
    fun provideGetContactList(contactRepository: ContactRepository): GetContactList {
        return GetContactList(contactRepository = contactRepository)
    }

    @Provides
    @Singleton
    fun provideContactRepository(): ContactRepository {
        return FakeContactRepositoryImpl()
    }

}