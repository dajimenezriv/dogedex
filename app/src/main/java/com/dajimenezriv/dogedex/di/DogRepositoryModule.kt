package com.dajimenezriv.dogedex.di

import com.dajimenezriv.dogedex.doglist.DogRepository
import com.dajimenezriv.dogedex.doglist.DogRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
// inside we put the scope of the injection
// SingletonComponent is like our module can be seen from all our app
@InstallIn(SingletonComponent::class)
abstract class DogRepositoryModule {

    @Binds
    abstract fun bindDogRepository(
        dogRepository: DogRepository
    ): DogRepositoryInterface
}

// now everytime we inject our DogRepositoryInterface,
// hilt is going to inject the class dogRepository