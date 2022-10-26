package com.dajimenezriv.dogedex.doglist

import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.models.Dog
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.api.APIService
import com.dajimenezriv.dogedex.api.dto.AddDogToUserDTO
import com.dajimenezriv.dogedex.api.dto.DogDTOMapper
import com.dajimenezriv.dogedex.api.makeNetworkCall
import com.dajimenezriv.dogedex.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

interface DogRepositoryInterface {
    suspend fun getDogCollection(): APIResponseStatus<List<Dog>>
    suspend fun addDogToUser(dogId: Long): APIResponseStatus<Any>
    suspend fun getDogByMlId(mlDogId: String): APIResponseStatus<Dog>
}

class DogRepository @Inject constructor(
    private val apiService: APIService,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : DogRepositoryInterface {
    override suspend fun getDogCollection(): APIResponseStatus<List<Dog>> {
        return withContext(dispatcher) {
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
            Dog(
                it.id,
                it.index,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                false,
            )
        }
    }.sorted()

    suspend fun downloadDogs(): APIResponseStatus<List<Dog>> {
        return makeNetworkCall {
            val dogListAPIResponse = apiService.getAllDogs()
            val dogDTOList = dogListAPIResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }
    }

    suspend fun getUserDogs(): APIResponseStatus<List<Dog>> {
        return makeNetworkCall {
            val dogListAPIResponse = apiService.getUserDogs()
            val dogDTOList = dogListAPIResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }
    }

    override suspend fun addDogToUser(dogId: Long): APIResponseStatus<Any> {
        return makeNetworkCall {
            val addDogToUserDTO = AddDogToUserDTO(dogId)
            val response = apiService.addDogToUser(addDogToUserDTO)
            if (!response.isSuccess) {
                throw Exception(response.message)
            }
        }
    }

    override suspend fun getDogByMlId(mlDogId: String): APIResponseStatus<Dog> {
        return makeNetworkCall {
            val response = apiService.getDogMyMlId(mlDogId)
            if (!response.isSuccess) {
                throw Exception(response.message)
            }
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOtoDogDomain(response.data.dog)
        }
    }
}
