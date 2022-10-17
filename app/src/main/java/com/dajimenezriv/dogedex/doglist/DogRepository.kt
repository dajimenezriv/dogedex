package com.dajimenezriv.dogedex.doglist

import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.models.Dog
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.api.DogsAPI.retrofitService
import com.dajimenezriv.dogedex.api.dto.AddDogToUserDTO
import com.dajimenezriv.dogedex.api.dto.DogDTOMapper
import com.dajimenezriv.dogedex.api.makeNetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Exception

class DogRepository {
    suspend fun getDogCollection(): APIResponseStatus<List<Dog>> {
        return withContext(Dispatchers.IO) {
            val allDogsListDeferred = async { downloadDogs() }
            val userDogsListDeferred = async { getUserDogs() }

            val allDogsListResponse = allDogsListDeferred.await()
            val userDogsListResponse = userDogsListDeferred.await()

            if (allDogsListResponse is APIResponseStatus.Error) {
                allDogsListResponse
            } else if (userDogsListResponse is APIResponseStatus.Error) {
                userDogsListResponse
            } else if (allDogsListResponse is APIResponseStatus.Success && userDogsListResponse is APIResponseStatus.Success) {
                print(userDogsListResponse)
                APIResponseStatus.Success(
                    getCollectionList(
                        allDogsListResponse.data,
                        userDogsListResponse.data
                    )
                )
            } else {
                APIResponseStatus.Error(R.string.error_unknown_error)
            }
        }
    }

    private fun getCollectionList(allDogList: List<Dog>, userDogList: List<Dog>) = allDogList.map {
        if (userDogList.contains(it)) {
            it
        } else {
            Dog(it.id, it.index, "", "", "", "", "", "", "", "", "", false)
        }
    }.sorted()

    suspend fun downloadDogs(): APIResponseStatus<List<Dog>> {
        return makeNetworkCall {
            val dogListAPIResponse = retrofitService.getAllDogs()
            val dogDTOList = dogListAPIResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }
    }

    suspend fun getUserDogs(): APIResponseStatus<List<Dog>> {
        return makeNetworkCall {
            val dogListAPIResponse = retrofitService.getUserDogs()
            val dogDTOList = dogListAPIResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }
    }

    suspend fun addDogToUser(dogId: Long): APIResponseStatus<Any> {
        return makeNetworkCall {
            val addDogToUserDTO = AddDogToUserDTO(dogId)
            val defaultResponse = retrofitService.addDogToUser(addDogToUserDTO)
            if (!defaultResponse.isSuccess) {
                throw Exception(defaultResponse.message)
            }
        }
    }
}
