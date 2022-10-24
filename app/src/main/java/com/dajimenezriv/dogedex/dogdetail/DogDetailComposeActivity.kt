package com.dajimenezriv.dogedex.dogdetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.dogdetail.ui.theme.DogedexTheme
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
        val isRecognition = intent?.extras?.g etBoolean(IS_RECOGNITION_KEY, false) ?: false

        if (dog == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish() // finish Activity
            return // and finish the code here
        }

        setContent {
            DogedexTheme {
                DogDetailScreen(dog = dog, viewModel.status.value)
            }
        }
    }
}
