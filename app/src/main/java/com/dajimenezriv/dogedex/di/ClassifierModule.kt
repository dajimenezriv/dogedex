package com.dajimenezriv.dogedex.di

import com.dajimenezriv.dogedex.main.ClassifierRepository
import com.dajimenezriv.dogedex.main.ClassifierRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ClassifierModule {

    @Binds
    abstract fun bindClassifierRepository(
        classifierRepository: ClassifierRepository
    ): ClassifierRepositoryInterface
}