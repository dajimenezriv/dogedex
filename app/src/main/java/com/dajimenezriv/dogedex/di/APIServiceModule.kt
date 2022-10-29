package com.dajimenezriv.dogedex.di

import com.dajimenezriv.dogedex.BASE_URL
import com.dajimenezriv.dogedex.api.APIService
import com.dajimenezriv.dogedex.api.APIServiceInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object APIServiceModule {

    @Provides
    fun provideAPIService(retrofit: Retrofit) = retrofit.create(APIService::class.java)

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        // parse json in an object that we can use
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideOkHttpClient() = OkHttpClient
        .Builder()
        .addInterceptor(APIServiceInterceptor)
        .build()
}