package com.dajimenezriv.dogedex

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.auth.AuthRepositoryInterface
import com.dajimenezriv.dogedex.auth.AuthScreen
import com.dajimenezriv.dogedex.auth.AuthViewModel
import com.dajimenezriv.dogedex.models.User
import org.junit.Rule
import org.junit.Test

class AuthScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun openSignUpScreen() {
        class FakeAuthRepository : AuthRepositoryInterface {
            override suspend fun logIn(email: String, password: String): APIResponseStatus<User> {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(
                email: String,
                password: String,
                confirmPassword: String
            ): APIResponseStatus<User> {
                TODO("Not yet implemented")
            }
        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        composeTestRule.setContent {
            AuthScreen(onUserLoggedIn = {}, viewModel = viewModel)
        }

        composeTestRule.onNodeWithTag(testTag = "logInButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag(testTag = "signUpButton").performClick()
        composeTestRule.onNodeWithTag(testTag = "signUpButton").assertIsDisplayed()
    }
}