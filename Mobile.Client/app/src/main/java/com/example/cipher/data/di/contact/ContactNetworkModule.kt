package com.example.cipher.data.di.contact

import com.example.cipher.data.NetworkKeys.IDENTITY_SERVER_BASE_URL
import com.example.cipher.data.di.AuthenticatedClient
import com.example.cipher.data.di.NetworkModule
import com.example.cipher.data.local.db.AppDatabase
import com.example.cipher.data.remote.api.ContactApi
import com.example.cipher.data.remote.repository.ContactRepositoryImpl
import com.example.cipher.domain.repository.contact.ContactRepository
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
class ContactNetworkModule {

    @Provides
    @Singleton
    fun provideContactRepository(database: AppDatabase, contactApi: ContactApi): ContactRepository {
        return ContactRepositoryImpl(database = database, contactApi = contactApi)
    }

    @Provides
    @Singleton
    fun provideContactApi(
        @AuthenticatedClient okHttpClient: OkHttpClient,
        moshi: Moshi
    ): ContactApi {
        return Retrofit.Builder()
            .baseUrl(IDENTITY_SERVER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ContactApi::class.java)
    }

}