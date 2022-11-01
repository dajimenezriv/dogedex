package com.dajimenezriv.dogedex

import androidx.camera.core.ImageProxy
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.di.ClassifierModule
import com.dajimenezriv.dogedex.di.DogRepositoryModule
import com.dajimenezriv.dogedex.doglist.DogRepositoryInterface
import com.dajimenezriv.dogedex.machinelearning.DogRecognition
import com.dajimenezriv.dogedex.main.ClassifierRepositoryInterface
import com.dajimenezriv.dogedex.main.MainActivity
import com.dajimenezriv.dogedex.models.Dog
import com.dajimenezriv.dogedex.testutils.EspressoIdlingResource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@UninstallModules(DogRepositoryModule::class, ClassifierModule::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 2)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    class FakeDogRepository @Inject constructor() : DogRepositoryInterface {
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
            return APIResponseStatus.Success(
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
                )
            )
        }

        override suspend fun getProbableDogs(probableDogsIds: ArrayList<String>): Flow<APIResponseStatus<Dog>> {
            TODO("Not yet implemented")
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class DogRepositoryModule {

        @Binds
        abstract fun bindDogRepository(
            fakeDogRepository: FakeDogRepository
        ): DogRepositoryInterface
    }

    class FakeClassifierRepository @Inject constructor() : ClassifierRepositoryInterface {
        override suspend fun recognizeImage(imageProxy: ImageProxy): List<DogRecognition> {
            return listOf(DogRecognition("sample_dog", 80f))
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class ClassifierModule {

        @Binds
        abstract fun bindClassifierRepository(
            fakeClassifierRepository: FakeClassifierRepository
        ): ClassifierRepositoryInterface
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    // use cheat sheet for espresso

    @Test
    fun showAllFab() {
        onView(withId(R.id.take_photo_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.dog_list_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.settings_fab)).check(matches(isDisplayed()))
    }

    @Test
    fun dogListOpens() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.dog_list_fab)).perform(click())

        // here we have a little problem
        // the screen we want to open it's created with jetpack compose
        val string = context.getString(R.string.top_bar_title)
        composeTestRule.onNodeWithText(string).assertIsDisplayed()
    }

    @Test
    fun checkCamera() {
        onView(withId(R.id.take_photo_fab)).perform(click())
        composeTestRule.onNodeWithTag(testTag = "closeDetailsScreen").assertIsDisplayed()
    }
}
