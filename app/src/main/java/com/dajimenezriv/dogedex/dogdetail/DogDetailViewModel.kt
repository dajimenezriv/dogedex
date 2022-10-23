package com.dajimenezriv.dogedex.dogdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.doglist.DogRepository
import kotlinx.coroutines.launch

class DogDetailViewModel: ViewModel() {
    private val _status = MutableLiveData<APIResponseStatus<Any>>()
    val status: LiveData<APIResponseStatus<Any>> get() = _status

    private val dogRepository = DogRepository()

    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            _status.value = APIResponseStatus.Loading()
            val apiResponseStatus = dogRepository.addDogToUser(dogId)
            _status.value = apiResponseStatus
        }
    }
}