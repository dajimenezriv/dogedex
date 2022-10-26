package com.dajimenezriv.dogedex.dogdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.doglist.DogRepository
import com.dajimenezriv.dogedex.doglist.DogRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogDetailViewModel @Inject constructor(
    private val dogRepository: DogRepositoryInterface
) : ViewModel() {
    // private val _status = MutableLiveData<APIResponseStatus<Any>>()
    // val status: LiveData<APIResponseStatus<Any>> get() = _status
    var status = mutableStateOf<APIResponseStatus<Any>?>(null)
        private set

    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            status.value = APIResponseStatus.Loading()
            val apiResponseStatus = dogRepository.addDogToUser(dogId)
            status.value = apiResponseStatus
        }
    }

    fun resetAPIResponseStatus() {
        status.value = null
    }
}