package com.dajimenezriv.dogedex

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.doglist.DogListScreen
import com.dajimenezriv.dogedex.doglist.DogListViewModel
import com.dajimenezriv.dogedex.doglist.DogRepositoryInterface
import com.dajimenezriv.dogedex.models.Dog
import org.junit.Rule
import org.junit.Test

class DogListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingDogs() {
        class FakeDogRepository : DogRepositoryInterface {
            override suspend fun getDogCollection(): APIResponseStatus<List<Dog>> {
                return APIResponseStatus.Loading()
            }

            override suspend fun addDogToUser(dogId: Long): APIResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlDogId: String): APIResponseStatus<Dog> {
                TODO("Not yet implemented")
            }
        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        composeTestRule.setContent {
            DogListScreen(
                onNavigationIconClick = { /*TODO*/ },
                onDogClicked = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag(testTag = "loadingWheel").assertIsDisplayed()
    }

    @Test
    fun errorGettingDogs() {
        class FakeDogRepository : DogRepositoryInterface {
            override suspend fun getDogCollection(): APIResponseStatus<List<Dog>> {
                return APIResponseStatus.Error(messageId = R.string.there_was_an_error)
            }

            override suspend fun addDogToUser(dogId: Long): APIResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlDogId: String): APIResponseStatus<Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        composeTestRule.setContent {
            DogListScreen(
                onNavigationIconClick = { /*TODO*/ },
                onDogClicked = {},
                viewModel = viewModel
            )
        }

        // the problem here is that the text is dependant of the language
        // because there is no context, we can't do getStringResource
        composeTestRule.onNodeWithText(text = "There was an error").assertIsDisplayed()
    }

    @Test
    fun successGettingDogs() {
        class FakeDogRepository : DogRepositoryInterface {
            override suspend fun getDogCollection(): APIResponseStatus<List<Dog>> {
                return APIResponseStatus.Success(
                    listOf(
                        Dog(
                            1,
                            1,
                            "First Dog",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            true,
                        ), Dog(
                            2,
                            2,
                            "Second Dog",
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
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlDogId: String): APIResponseStatus<Dog> {
                TODO("Not yet implemented")
            }
        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        composeTestRule.setContent {
            DogListScreen(
                onNavigationIconClick = { /*TODO*/ },
                onDogClicked = {},
                viewModel = viewModel
            )
        }

        // with useUnmergedTree we use tags for every node
        // see in google for more detail
        // composeTestRule.onRoot(useUnmergedTree = true).printToLog("MANZANA")
        // the problem here is that the text is dependant of the language
        // because there is no context, we can't do getStringResource
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "dog-First Dog")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }
}