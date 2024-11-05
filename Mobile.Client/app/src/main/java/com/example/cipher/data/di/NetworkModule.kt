package com.example.cipher.data.di

import com.example.cipher.data.remote.api.dto.LocalDateTimeAdapter
import com.example.cipher.data.remote.interceptor.AccessTokenInterceptor
import com.microsoft.signalr.HubConnectionBuilder
import com.squareup.moshi.Moshi
import com.example.cipher.data.NetworkKeys.CHAT_SERVER_HUB_URL
import com.example.cipher.data.remote.repository.EventHubListenerImpl
import com.example.cipher.data.remote.repository.EventSubscriptionServiceImpl
import com.example.cipher.domain.repository.event.EventHubListener
import com.example.cipher.domain.repository.event.EventSubscriptionService
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.TransportEnum
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideAccessOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(accessTokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @AuthClient
    fun provideAuthOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(LocalDateTimeAdapter)
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideEvenSubscriptionService(eventHubListener: EventHubListener): EventSubscriptionService {
        return EventSubscriptionServiceImpl(eventHubListener = eventHubListener)
    }

    @Provides
    @Singleton
    fun provideEventHubListener(hubConnection: HubConnection): EventHubListener {
        return EventHubListenerImpl(hubConnection = hubConnection)
    }

    @Provides
    @Singleton
    fun provideHubConnection(): HubConnection {
        return HubConnectionBuilder
            .create(CHAT_SERVER_HUB_URL)
            .withTransport(TransportEnum.WEBSOCKETS)
            .build()
    }


}