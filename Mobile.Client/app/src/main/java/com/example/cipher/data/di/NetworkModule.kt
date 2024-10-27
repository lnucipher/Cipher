package com.example.cipher.data.di

import com.example.cipher.data.NetworkKeys.BASE_URL
import com.example.cipher.data.remote.api.AuthApi
import com.example.cipher.data.remote.interceptor.AccessTokenInterceptor
import com.example.cipher.data.remote.repository.AuthRepositoryImpl
import com.example.cipher.data.remote.repository.FakeContactRepositoryImpl
import com.example.cipher.data.remote.repository.FakeMessageRepositoryImpl
import com.example.cipher.data.remote.repository.FakeUserRepositoryImpl
import com.example.cipher.domain.repository.auth.AuthRepository
import com.example.cipher.domain.repository.auth.JwtTokenManager
import com.example.cipher.domain.repository.contact.ContactRepository
import com.example.cipher.domain.repository.contact.GetContactList
import com.example.cipher.domain.repository.message.GetMessageList
import com.example.cipher.domain.repository.message.MessageRepository
import com.example.cipher.domain.repository.user.LocalUserManager
import com.example.cipher.domain.repository.user.UserRepository
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideUserRepository (): UserRepository {
        return FakeUserRepositoryImpl()
    }

    //MESSAGES
    @Provides
    @Singleton
    fun provideGetMessageList(messageRepository: MessageRepository): GetMessageList {
        return GetMessageList(messageRepository = messageRepository)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(): MessageRepository {
        return FakeMessageRepositoryImpl()
    }

    //CONTACT
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

    //AUTH
    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi, tokenManager: JwtTokenManager, localUserManager: LocalUserManager): AuthRepository {
        return AuthRepositoryImpl(api = api, tokenManager = tokenManager, localUserManager = localUserManager)
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        @AuthClient okHttpClient: OkHttpClient,
        moshi: Moshi
    ): AuthApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthApi::class.java)
    }

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
            .add(KotlinJsonAdapterFactory())
            .build()

}