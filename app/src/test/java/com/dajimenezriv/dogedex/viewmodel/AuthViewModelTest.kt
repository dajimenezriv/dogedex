package com.dajimenezriv.dogedex.viewmodel

import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.auth.AuthRepositoryInterface
import com.dajimenezriv.dogedex.auth.AuthViewModel
import com.dajimenezriv.dogedex.models.User
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testSignUp() {
        val fakeUser = User(id = 1, email = "mail@mail.com", authenticationToken = "token")

        class FakeAuthRepository: AuthRepositoryInterface {
            override suspend fun logIn(email: String, password: String): APIResponseStatus<User> {
                return APIResponseStatus.Success(fakeUser)
            }

            override suspend fun signUp(
                email: String,
                password: String,
                confirmPassword: String
            ): APIResponseStatus<User> {
                return APIResponseStatus.Success(fakeUser)
            }

        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        viewModel.signUp("", "", "")
        assertEquals(R.string.email_is_not_valid, viewModel.emailError.value)
        assertEquals(R.string.password_is_not_valid, viewModel.passwordError.value)
        assertEquals(null, viewModel.confirmPasswordError.value)

        viewModel.signUp("", "testing", "testing2")
        assertEquals(R.string.passwords_do_not_match, viewModel.passwordError.value)
        assertEquals(R.string.passwords_do_not_match, viewModel.confirmPasswordError.value)

        viewModel.signUp("mail@mail.com", "testing", "testing")
        assertEquals(fakeUser.email, viewModel.user.value?.email)
        assert(viewModel.status.value is APIResponseStatus.Success)
    }
}