package com.dajimenezriv.dogedex.doglist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dajimenezriv.dogedex.models.Dog
import com.dajimenezriv.dogedex.api.APIResponseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
// hilt knows that he has to inject a viewModel with an empty constructor
class DogListViewModel @Inject constructor(
    private val dogRepository: DogRepositoryInterface,
): ViewModel() {
    // private val _dogs = MutableLiveData<List<Dog>>()
    // val dogs: LiveData<List<Dog>> get() = _dogs

    var dogs = mutableStateOf<List<Dog>>(listOf())
        private set

    // private val _status = MutableLiveData<APIResponseStatus<Any>>()
    // val status: LiveData<APIResponseStatus<Any>> get() = _status

    var status = mutableStateOf<APIResponseStatus<Any>?>(null)
        private set

    init {
        getDogCollection()
    }

    fun resetAPIResponseStatus() {
        status.value = null
    }

    @Suppress("UNCHECKED_CAST")
    private fun getDogCollection() {
        viewModelScope.launch {
            status.value = APIResponseStatus.Loading()
            val apiResponseStatus = dogRepository.getDogCollection()
            if (apiResponseStatus is APIResponseStatus.Success)
                dogs.value = apiResponseStatus.data
            status.value = apiResponseStatus as APIResponseStatus<Any>
        }
    }
}