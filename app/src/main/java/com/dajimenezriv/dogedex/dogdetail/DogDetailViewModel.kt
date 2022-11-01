package com.dajimenezriv.dogedex.dogdetail

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.dogdetail.DogDetailComposeActivity.Companion.DOG_KEY
import com.dajimenezriv.dogedex.dogdetail.DogDetailComposeActivity.Companion.IS_RECOGNITION_KEY
import com.dajimenezriv.dogedex.dogdetail.DogDetailComposeActivity.Companion.PROBABLE_DOGS_IDS
import com.dajimenezriv.dogedex.doglist.DogRepositoryInterface
import com.dajimenezriv.dogedex.models.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class DogDetailViewModel @Inject constructor(
    private val dogRepository: DogRepositoryInterface,
    // when the app is closed for example, abruptly by the android system
    // savedStateHandle saves our current state
    // we are going to use it to access the variables from the screen directly
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var probableDogsIds =
        mutableStateListOf(savedStateHandle.get<ArrayList<String>>(PROBABLE_DOGS_IDS))

    private var _probableDogList = MutableStateFlow<MutableList<Dog>>(mutableListOf())
    val probableDogList: StateFlow<MutableList<Dog>>
        get() = _probableDogList

    var dog = mutableStateOf(savedStateHandle.get<Dog>(DOG_KEY))
        private set

    var isRecognition = mutableStateOf(savedStateHandle.get<Boolean>(IS_RECOGNITION_KEY))
        private set

    // private val _status = MutableLiveData<APIResponseStatus<Any>>()
    // val status: LiveData<APIResponseStatus<Any>> get() = _status
    var status = mutableStateOf<APIResponseStatus<Any>?>(null)
        private set

    fun getProbableDogs() {
        _probableDogList.value.clear()
        viewModelScope.launch {
            // we have to check if probableDogsIds is not null
            probableDogsIds[0]?.let {
                dogRepository.getProbableDogs(it)
                    // this is the official way to manage exceptions with flows
                    // we can use this way to catch exceptions
                    // instead of using the file makeNetworkCall
                    .catch { exception ->
                        if (exception is UnknownHostException) {
                            status.value =
                                APIResponseStatus.Error(R.string.error_unknown_host_exception)
                        }
                    }
                    .collect { apiResponseStatus ->
                        if (apiResponseStatus is APIResponseStatus.Success) {
                            // we are just using a helper list
                            val probableDogMutableList = _probableDogList.value.toMutableList()
                            probableDogMutableList.add(apiResponseStatus.data)
                            _probableDogList.value = probableDogMutableList
                        }
                    }
            }
        }
    }

    fun updateDog(newDog: Dog) {
        dog.value = newDog
    }

    fun addDogToUser() {
        viewModelScope.launch {
            status.value = APIResponseStatus.Loading()
            val apiResponseStatus = dogRepository.addDogToUser(dog.value!!.id)
            status.value = apiResponseStatus
        }
    }

    fun resetAPIResponseStatus() {
        status.value = null
    }
}