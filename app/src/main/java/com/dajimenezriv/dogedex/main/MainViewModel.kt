package com.dajimenezriv.dogedex.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.doglist.DogRepository
import com.dajimenezriv.dogedex.models.Dog
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _dog = MutableLiveData<Dog>()
    val dog: LiveData<Dog> get() = _dog

    private val _status = MutableLiveData<APIResponseStatus<Dog>>()
    val status: LiveData<APIResponseStatus<Dog>> get() = _status

    private val dogRepository = DogRepository()

    fun getDogByMlId(mlDogId: String) {
        viewModelScope.launch {
            val apiResponseStatus = dogRepository.getDogByMlId(mlDogId)
            if (apiResponseStatus is APIResponseStatus.Success) {
                _dog.value = apiResponseStatus.data!!
            }
            _status.value = apiResponseStatus
        }
    }
}