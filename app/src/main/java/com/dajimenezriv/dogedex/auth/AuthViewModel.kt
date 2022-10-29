package com.dajimenezriv.dogedex.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInterface
) : ViewModel() {
    // private val _user = MutableLiveData<User>()
    // val user: LiveData<User> get() = _user

    var user = mutableStateOf<User?>(null)
        private set

    var emailError = mutableStateOf<Int?>(null)
        private set

    var passwordError = mutableStateOf<Int?>(null)
        private set

    var confirmPasswordError = mutableStateOf<Int?>(null)
        private set


    // private val _status = MutableLiveData<APIResponseStatus<User>>()
    // val status: LiveData<APIResponseStatus<User>> get() = _status

    var status = mutableStateOf<APIResponseStatus<User>?>(null)
        private set

    fun resetAPIResponseStatus() {
        status.value = null
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        // it's going to call the method in our repository to signUp
        // after all processes, the repository is going to bring an APIResponseStatus
        // that we can check for the different states
        // also the variable data that contains the real information

        resetErrors()
        var error = false

        if (email.isEmpty()) {
            error = true
            emailError.value = R.string.email_is_not_valid
        }

        if (password.isEmpty()) {
            error = true
            passwordError.value = R.string.password_is_not_valid
        }

        if (password != confirmPassword) {
            error = true
            passwordError.value = R.string.passwords_do_not_match
            confirmPasswordError.value = R.string.passwords_do_not_match
        }

        if (error) return

        // inside a coroutine (in background, not the main thread)
        viewModelScope.launch {
            status.value = APIResponseStatus.Loading()
            handleResponseStatus(authRepository.signUp(email, password, confirmPassword))
        }
    }

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            status.value = APIResponseStatus.Loading()
            handleResponseStatus(authRepository.logIn(email, password))
        }
    }

    private fun handleResponseStatus(apiResponseStatus: APIResponseStatus<User>) {
        if (apiResponseStatus is APIResponseStatus.Success) {
            user.value = apiResponseStatus.data
        }

        status.value = apiResponseStatus
    }

    private fun resetErrors() {
        emailError.value = null
        passwordError.value = null
        confirmPasswordError.value = null
    }
}