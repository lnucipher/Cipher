package com.example.cipher.data.di.user

import com.example.cipher.data.NetworkKeys.IDENTITY_SERVER_BASE_URL
import com.example.cipher.data.di.AuthenticatedClient
import com.example.cipher.data.di.NetworkModule
import com.example.cipher.data.remote.api.UserApi
import com.example.cipher.data.remote.repository.UserRepositoryImpl
import com.example.cipher.domain.repository.user.UserRepository
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
class UserNetworkModule {

    @Provides
    @Singleton
    fun provideUserRepository (userApi: UserApi): UserRepository {
        return UserRepositoryImpl(userApi = userApi)
    }

    @Provides
    @Singleton
    fun provideUserApi(
        @AuthenticatedClient okHttpClient: OkHttpClient,
        moshi: Moshi
    ): UserApi {
        return Retrofit.Builder()
            .baseUrl(IDENTITY_SERVER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(UserApi::class.java)
    }
}