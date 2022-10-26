package com.dajimenezriv.dogedex.main

import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.doglist.DogRepository
import com.dajimenezriv.dogedex.doglist.DogRepositoryInterface
import com.dajimenezriv.dogedex.machinelearning.Classifier
import com.dajimenezriv.dogedex.machinelearning.DogRecognition
import com.dajimenezriv.dogedex.models.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.nio.MappedByteBuffer
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dogRepository: DogRepositoryInterface
): ViewModel() {
    private val _dog = MutableLiveData<Dog>()
    val dog: LiveData<Dog> get() = _dog

    private val _status = MutableLiveData<APIResponseStatus<Dog>>()
    val status: LiveData<APIResponseStatus<Dog>> get() = _status

    private val _dogRecognition = MutableLiveData<DogRecognition>()
    val dogRecognition: LiveData<DogRecognition> get() = _dogRecognition

    private lateinit var classifierRepository: ClassifierRepository

    fun setupClassifier(tfLiteModel: MappedByteBuffer, labels: List<String>) {
        val classifier = Classifier(tfLiteModel, labels)
        classifierRepository = ClassifierRepository(classifier)
    }

    fun recognizeImage(imageProxy: ImageProxy) {
        viewModelScope.launch {
            _dogRecognition.value = classifierRepository.recognizeImage(imageProxy)
            imageProxy.close()
        }
    }

    fun getDogByMlId(mlDogId: String) {
        viewModelScope.launch {
            val apiResponseStatus = dogRepository.getDogByMlId(mlDogId)
            if (apiResponseStatus is APIResponseStatus.Success) {
                _dog.value = apiResponseStatus.data
            }
            _status.value = apiResponseStatus
        }
    }
}