package com.dajimenezriv.dogedex.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.models.User
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _status = MutableLiveData<APIResponseStatus<User>>()
    val status: LiveData<APIResponseStatus<User>> get() = _status

    private val authRepository = AuthRepository()

    fun signUp(email: String, password: String, confirmPassword: String) {
        // it's going to call the method in our repository to signUp
        // after all processes, the repository is going to bring an APIResponseStatus
        // that we can check for the different states
        // also the variable data that contains the real information

        // inside a coroutine (in background, not the main thread)
        viewModelScope.launch {
            _status.value = APIResponseStatus.Loading()
            handleResponseStatus(authRepository.signUp(email, password, confirmPassword))
        }
    }

    private fun handleResponseStatus(apiResponseStatus: APIResponseStatus<User>) {
        if (apiResponseStatus is APIResponseStatus.Success) {
            _user.value = apiResponseStatus.data
        }

        _status.value = apiResponseStatus
    }
}