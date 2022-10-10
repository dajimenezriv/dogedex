package com.dajimenezriv.dogedex.doglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dajimenezriv.dogedex.Dog
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {
    private val _dogs = MutableLiveData<List<Dog>>()
    val dogs: LiveData<List<Dog>> get() = _dogs

    private val dogRepository = DogRepository()

    init {
        downloadDogs()
    }

    private fun downloadDogs() {
        // to download data from internet we need to use coroutines
        viewModelScope.launch {
            _dogs.value = dogRepository.downloadDogs()
        }
    }
}