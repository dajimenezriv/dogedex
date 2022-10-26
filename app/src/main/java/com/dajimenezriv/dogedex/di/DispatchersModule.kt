package com.dajimenezriv.dogedex.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    // in case we need to use multiple dispatchers with inject
    @IODispatcher
    @Provides
    fun provideIODispatcher() = Dispatchers.IO
}

annotation class IODispatcher
