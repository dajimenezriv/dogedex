package com.dajimenezriv.dogedex

import androidx.camera.core.ImageProxy
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.di.ClassifierModule
import com.dajimenezriv.dogedex.di.DogRepositoryModule
import com.dajimenezriv.dogedex.doglist.DogRepositoryInterface
import com.dajimenezriv.dogedex.machinelearning.DogRecognition
import com.dajimenezriv.dogedex.main.ClassifierRepositoryInterface
import com.dajimenezriv.dogedex.main.MainActivity
import com.dajimenezriv.dogedex.models.Dog
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@UninstallModules(DogRepositoryModule::class, ClassifierModule::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
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
        override suspend fun recognizeImage(imageProxy: ImageProxy): DogRecognition {
            TODO("Not yet implemented")
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

    @Test
    fun showAllFab() {
        onView(withId(R.id.take_photo_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.dog_list_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.settings_fab)).check(matches(isDisplayed()))
    }
}
