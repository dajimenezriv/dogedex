package com.dajimenezriv.dogedex.di

import com.dajimenezriv.dogedex.auth.AuthRepository
import com.dajimenezriv.dogedex.auth.AuthRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepository: AuthRepository
    ): AuthRepositoryInterface
}