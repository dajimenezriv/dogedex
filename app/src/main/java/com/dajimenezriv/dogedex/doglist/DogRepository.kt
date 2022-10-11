package com.dajimenezriv.dogedex.doglist

import com.dajimenezriv.dogedex.Dog
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.api.DogsAPI.retrofitService
import com.dajimenezriv.dogedex.api.dto.DogDTOMapper
import com.dajimenezriv.dogedex.api.makeNetworkCall

class DogRepository {
    suspend fun downloadDogs(): APIResponseStatus<List<Dog>> {
        return makeNetworkCall {
            val dogListAPIResponse = retrofitService.getAllDogs()
            val dogDTOList = dogListAPIResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }
    }
}
