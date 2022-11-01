package com.dajimenezriv.dogedex.viewmodel

import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.doglist.DogListViewModel
import com.dajimenezriv.dogedex.doglist.DogRepositoryInterface
import com.dajimenezriv.dogedex.models.Dog
import kotlinx.coroutines.flow.Flow
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class DogListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun downloadDogListStatusCorrect() {
        class FakeDogRepository : DogRepositoryInterface {
            override suspend fun getDogCollection(): APIResponseStatus<List<Dog>> {
                return APIResponseStatus.Success(
                    listOf(
                        Dog(
                            1,
                            1,
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
                        ),
                        Dog(
                            2,
                            2,
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
                    )
                )
            }

            override suspend fun addDogToUser(dogId: Long): APIResponseStatus<Any> {
                return APIResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMlId(mlDogId: String): APIResponseStatus<Dog> {
                return APIResponseStatus.Success(
                    Dog(
                        1,
                        1,
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
                    ),
                )
            }

            override suspend fun getProbableDogs(probableDogsIds: ArrayList<String>): Flow<APIResponseStatus<Dog>> {
                TODO("Not yet implemented")
            }
        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        assertEquals(2, viewModel.dogs.value.size)
        assert(viewModel.status.value is APIResponseStatus.Success)
    }

    @Test
    fun downloadDogListStatusError() {
        class FakeDogRepository : DogRepositoryInterface {
            override suspend fun getDogCollection(): APIResponseStatus<List<Dog>> {
                return APIResponseStatus.Error(messageId = 12)
            }

            override suspend fun addDogToUser(dogId: Long): APIResponseStatus<Any> {
                return APIResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMlId(mlDogId: String): APIResponseStatus<Dog> {
                return APIResponseStatus.Success(
                    Dog(
                        1,
                        1,
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
                    ),
                )
            }

            override suspend fun getProbableDogs(probableDogsIds: ArrayList<String>): Flow<APIResponseStatus<Dog>> {
                TODO("Not yet implemented")
            }
        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        assertEquals(0, viewModel.dogs.value.size)
        assert(viewModel.status.value is APIResponseStatus.Error)
    }
}