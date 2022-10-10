package com.dajimenezriv.dogedex.doglist

import com.dajimenezriv.dogedex.Dog
import com.dajimenezriv.dogedex.api.DogsAPI.retrofitService
import com.dajimenezriv.dogedex.api.dto.DogDTOMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DogRepository {
    suspend fun downloadDogs(): List<Dog> {
        return withContext(Dispatchers.IO) {
            val dogListAPIResponse = retrofitService.getAllDogs()
            val dogDTOList = dogListAPIResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }
    }
}
