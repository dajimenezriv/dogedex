package com.dajimenezriv.dogedex.dogdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.doglist.DogRepository
import kotlinx.coroutines.launch

class DogDetailViewModel: ViewModel() {
    // private val _status = MutableLiveData<APIResponseStatus<Any>>()
    // val status: LiveData<APIResponseStatus<Any>> get() = _status
    var status = mutableStateOf<APIResponseStatus<Any>?>(APIResponseStatus.Loading())
        private set

    private val dogRepository = DogRepository()

    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            status.value = APIResponseStatus.Loading()
            val apiResponseStatus = dogRepository.addDogToUser(dogId)
            status.value = apiResponseStatus
        }
    }
}