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

    private val _status = MutableLiveData<APIResponseStatus<List<Dog>>>()
    val status: LiveData<APIResponseStatus<List<Dog>>> get() = _status

    private val dogRepository = DogRepository()

    init {
        downloadDogs()
    }

    private fun downloadDogs() {
        // to download data from internet we need to use coroutines
        viewModelScope.launch {
            _status.value = APIResponseStatus.Loading()
            handleResponseStatus(dogRepository.downloadDogs())
        }
    }

    private fun handleResponseStatus(apiResponseStatus: APIResponseStatus<List<Dog>>) {
        if (apiResponseStatus is APIResponseStatus.Success) {
            _dogs.value = apiResponseStatus.data
        }

        _status.value = apiResponseStatus
    }
}