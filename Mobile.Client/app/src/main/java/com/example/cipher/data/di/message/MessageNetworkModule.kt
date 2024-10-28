package com.example.cipher.data.di.message

import com.example.cipher.data.NetworkKeys.CHAT_SERVER_BASE_URL
import com.example.cipher.data.di.AuthenticatedClient
import com.example.cipher.data.di.NetworkModule
import com.example.cipher.data.local.db.AppDatabase
import com.example.cipher.data.remote.api.MessageApi
import com.example.cipher.data.remote.repository.FakeMessageRepositoryImpl
import com.example.cipher.data.remote.repository.MessageRepositoryImpl
import com.example.cipher.domain.repository.message.GetMessageList
import com.example.cipher.domain.repository.message.MessageRepository
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class MessageNetworkModule {

    @Provides
    @Singleton
    fun provideGetMessageList(messageRepository: MessageRepository): GetMessageList {
        return GetMessageList(messageRepository = messageRepository)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(database: AppDatabase, messageApi: MessageApi): MessageRepository {
        return MessageRepositoryImpl(database = database, messageApi = messageApi)
    }

    @Provides
    @Singleton
    fun provideMessageApi(
        @AuthenticatedClient okHttpClient: OkHttpClient,
        moshi: Moshi
    ): MessageApi {
        return Retrofit.Builder()
            .baseUrl(CHAT_SERVER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MessageApi::class.java)
    }
}