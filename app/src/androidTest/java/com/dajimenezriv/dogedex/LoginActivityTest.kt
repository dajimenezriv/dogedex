package com.dajimenezriv.dogedex

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.auth.AuthRepositoryInterface
import com.dajimenezriv.dogedex.auth.LoginComposeActivity
import com.dajimenezriv.dogedex.di.AuthRepositoryModule
import com.dajimenezriv.dogedex.models.User
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

// we need to uninstall the default repository injected
// and inject another one for testing
@UninstallModules(AuthRepositoryModule::class)
@HiltAndroidTest
class LoginActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<LoginComposeActivity>()

    class FakeAuthRepository @Inject constructor() : AuthRepositoryInterface {
        override suspend fun logIn(email: String, password: String): APIResponseStatus<User> {
            return APIResponseStatus.Success(
                User(1L, "testing@mail.com", "token")
            )
        }

        override suspend fun signUp(
            email: String,
            password: String,
            confirmPassword: String
        ): APIResponseStatus<User> {
            TODO("Not yet implemented")
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class AuthRepositoryModule {
        @Binds
        abstract fun bindAuthRepository(
            fakeAuthRepository: FakeAuthRepository
        ): AuthRepositoryInterface
    }

    @Test
    fun logIn() {
        val context = composeTestRule.activity

        // jetpack compose
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "logInButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "emailInput").performTextClearance()
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "emailInput").performTextInput("testing@gmail.com")
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "passwordInput").performTextClearance()
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "passwordInput").performTextInput("testing")
        composeTestRule.onNodeWithTag(testTag = "logInButton").performClick()

        // espresso
        onView(withId(R.id.take_photo_fab)).check(matches(isDisplayed()))
    }
}