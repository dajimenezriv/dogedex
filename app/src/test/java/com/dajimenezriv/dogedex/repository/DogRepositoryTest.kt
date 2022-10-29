package com.dajimenezriv.dogedex.repository

import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.api.APIService
import com.dajimenezriv.dogedex.api.dto.AddDogToUserDTO
import com.dajimenezriv.dogedex.api.dto.DogDTO
import com.dajimenezriv.dogedex.api.dto.LoginDTO
import com.dajimenezriv.dogedex.api.dto.SignUpDTO
import com.dajimenezriv.dogedex.api.responses.*
import com.dajimenezriv.dogedex.doglist.DogRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Test
import java.net.UnknownHostException

class DogRepositoryTest {
    @Test
    fun testGetDogCollectionSuccess(): Unit = runBlocking {
        class FakeAPIService : APIService {
            override suspend fun getAllDogs(): DogListAPIResponse {
                return DogListAPIResponse(
                    message = "",
                    isSuccess = true,
                    data = DogListResponse(
                        listOf(
                            DogDTO(
                                1,
                                1,
                                "FirstDog",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                            ),
                            DogDTO(
                                2,
                                2,
                                "SecondDog",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                            )
                        )
                    )
                )
            }

            override suspend fun signUp(signUpDTO: SignUpDTO): AuthAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: LoginDTO): AuthAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: AddDogToUserDTO): DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserDogs(): DogListAPIResponse {
                return DogListAPIResponse(
                    message = "",
                    isSuccess = true,
                    data = DogListResponse(
                        listOf(
                            DogDTO(
                                2,
                                2,
                                "SecondDog",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                            )
                        )
                    )
                )
            }

            override suspend fun getDogMyMlId(mlId: String): DogAPIResponse {
                TODO("Not yet implemented")
            }

        }

        val dogRepository = DogRepository(
            apiService = FakeAPIService(),
            dispatcher = TestCoroutineDispatcher()
        )

        // however, the method is a suspend function
        // with runBlocking, we are telling the test that it should execute it in the main thread
        // and wait until the response
        val apiResponseStatus = dogRepository.getDogCollection()
        assert(apiResponseStatus is APIResponseStatus.Success)
        val dogCollection = (apiResponseStatus as APIResponseStatus.Success).data
        assertEquals(2, dogCollection.size)
        // the first dog is called FirstDog, however, since it's not in the collection
        // it returns an empty dog
        assertEquals("", dogCollection[0].name)
        assertEquals("SecondDog", dogCollection[1].name)
    }

    @Test
    fun testGetAllDogsError(): Unit = runBlocking {
        class FakeAPIService : APIService {
            override suspend fun getAllDogs(): DogListAPIResponse {
                throw UnknownHostException()
            }

            override suspend fun signUp(signUpDTO: SignUpDTO): AuthAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: LoginDTO): AuthAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: AddDogToUserDTO): DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserDogs(): DogListAPIResponse {
                return DogListAPIResponse(
                    message = "",
                    isSuccess = true,
                    data = DogListResponse(
                        listOf(
                            DogDTO(
                                2,
                                2,
                                "SecondDog",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                            )
                        )
                    )
                )
            }

            override suspend fun getDogMyMlId(mlId: String): DogAPIResponse {
                TODO("Not yet implemented")
            }

        }

        val dogRepository = DogRepository(
            apiService = FakeAPIService(),
            dispatcher = TestCoroutineDispatcher()
        )

        // however, the method is a suspend function
        // with runBlocking, we are telling the test that it should execute it in the main thread
        // and wait until the response
        val apiResponseStatus = dogRepository.getDogCollection()
        assert(apiResponseStatus is APIResponseStatus.Error)
        assertEquals(
            R.string.error_unknown_host_exception,
            (apiResponseStatus as APIResponseStatus.Error).messageId
        )
    }

    @Test
    fun getDogByMlSuccess() = runBlocking {
        class FakeAPIService : APIService {
            override suspend fun getAllDogs(): DogListAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(signUpDTO: SignUpDTO): AuthAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: LoginDTO): AuthAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: AddDogToUserDTO): DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserDogs(): DogListAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getDogMyMlId(mlId: String): DogAPIResponse {
                return DogAPIResponse(
                    message = "",
                    isSuccess = true,
                    data = DogResponse(
                        DogDTO(
                            1,
                            1,
                            "FirstDog",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                        ),
                    )
                )
            }
        }

        val dogRepository = DogRepository(
            apiService = FakeAPIService(),
            dispatcher = TestCoroutineDispatcher()
        )

        val apiResponseStatus = dogRepository.getDogByMlId("id")
        assert(apiResponseStatus is APIResponseStatus.Success)
        assertEquals(1, (apiResponseStatus as APIResponseStatus.Success).data.id)
    }

    @Test
    fun getDogByMlError() = runBlocking {
        class FakeAPIService : APIService {
            override suspend fun getAllDogs(): DogListAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(signUpDTO: SignUpDTO): AuthAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: LoginDTO): AuthAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: AddDogToUserDTO): DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserDogs(): DogListAPIResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getDogMyMlId(mlId: String): DogAPIResponse {
                return DogAPIResponse(
                    message = "error_getting_dog_ml_id",
                    isSuccess = false,
                    data = DogResponse(
                        DogDTO(
                            1,
                            1,
                            "FirstDog",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                        ),
                    )
                )
            }
        }

        val dogRepository = DogRepository(
            apiService = FakeAPIService(),
            dispatcher = TestCoroutineDispatcher()
        )

        val apiResponseStatus = dogRepository.getDogByMlId("id")
        assert(apiResponseStatus is APIResponseStatus.Error)
        assertEquals(R.string.error_unknown_error, (apiResponseStatus as APIResponseStatus.Error).messageId)
    }
}