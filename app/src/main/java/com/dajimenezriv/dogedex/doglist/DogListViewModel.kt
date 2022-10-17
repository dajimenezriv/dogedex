package com.dajimenezriv.dogedex.doglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dajimenezriv.dogedex.models.Dog
import com.dajimenezriv.dogedex.api.APIResponseStatus
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {
    private val _dogs = MutableLiveData<List<Dog>>()
    val dogs: LiveData<List<Dog>> get() = _dogs

    private val _status = MutableLiveData<APIResponseStatus<Any>>()
    val status: LiveData<APIResponseStatus<Any>> get() = _status

    private val dogRepository = DogRepository()

    init {
        getDogCollection()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getDogCollection() {
        viewModelScope.launch {
            _status.value = APIResponseStatus.Loading()
            val apiResponseStatus = dogRepository.getDogCollection()
            if (apiResponseStatus is APIResponseStatus.Success)
                _dogs.value = apiResponseStatus.data
            _status.value = apiResponseStatus as APIResponseStatus<Any>
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun downloadDogs() {
        // to download data from internet we need to use coroutines
        viewModelScope.launch {
            _status.value = APIResponseStatus.Loading()
            val apiResponseStatus = dogRepository.downloadDogs()
            if (apiResponseStatus is APIResponseStatus.Success)
                _dogs.value = apiResponseStatus.data
            _status.value = apiResponseStatus as APIResponseStatus<Any>
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getUserDogs() {
        viewModelScope.launch {
            _status.value = APIResponseStatus.Loading()
            val apiResponseStatus = dogRepository.getUserDogs()
            if (apiResponseStatus is APIResponseStatus.Success)
                _dogs.value = apiResponseStatus.data
            _status.value = apiResponseStatus as APIResponseStatus<Any>
        }
    }

    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            _status.value = APIResponseStatus.Loading()
            val apiResponseStatus = dogRepository.addDogToUser(dogId)
            if (apiResponseStatus is APIResponseStatus.Success) getDogCollection()
            _status.value = apiResponseStatus
        }
    }
}