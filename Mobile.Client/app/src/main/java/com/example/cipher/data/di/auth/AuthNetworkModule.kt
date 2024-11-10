package com.example.cipher.data.di.auth

import com.example.cipher.data.NetworkKeys.IDENTITY_SERVER_BASE_URL
import com.example.cipher.data.di.AuthClient
import com.example.cipher.data.di.NetworkModule
import com.example.cipher.data.remote.api.AuthApi
import com.example.cipher.data.remote.repository.AuthRepositoryImpl
import com.example.cipher.domain.repository.auth.AuthRepository
import com.example.cipher.domain.repository.auth.JwtTokenManager
import com.example.cipher.domain.repository.user.LocalUserManager
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
class AuthNetworkModule {

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
            .baseUrl(IDENTITY_SERVER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthApi::class.java)
    }
}