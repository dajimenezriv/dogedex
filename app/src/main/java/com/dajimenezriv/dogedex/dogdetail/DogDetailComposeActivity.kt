package com.dajimenezriv.dogedex.dogdetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.dogdetail.ui.theme.DogedexTheme
import com.dajimenezriv.dogedex.machinelearning.DogRecognition
import com.dajimenezriv.dogedex.models.Dog

class DogDetailComposeActivity : ComponentActivity() {
    companion object {
        const val DOG_KEY = "dog"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }

    private val viewModel: DogDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
        val isRecognition = intent?.extras?.getBoolean(IS_RECOGNITION_KEY, false) ?: false

        if (dog == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish() // finish Activity
            return // and finish the code here
        }

        setContent {
            // when we add a dog to our collection (when we execute the bottom onClick function)
            // after the viewModel saves the dog, the viewModel status changes to Success
            val status = viewModel.status
            if (status.value is APIResponseStatus.Success) finish()
            else {
                DogedexTheme {
                    DogDetailScreen(
                        dog = dog,
                        status = status.value,
                        onFloatButtonClick = { onClick(dog.id, isRecognition) },
                        onDialogDismiss = { viewModel.resetAPIResponseStatus()}
                    )
                }
            }
        }
    }

    private fun onClick(dogId: Long, isRecognition: Boolean) {
        if (isRecognition) viewModel.addDogToUser(dogId)
        else finish()
    }
}
