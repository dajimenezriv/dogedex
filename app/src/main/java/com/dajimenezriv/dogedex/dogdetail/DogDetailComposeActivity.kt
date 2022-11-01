package com.dajimenezriv.dogedex.dogdetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dajimenezriv.dogedex.dogdetail.ui.theme.DogedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogDetailComposeActivity : ComponentActivity() {
    companion object {
        const val DOG_KEY = "dog"
        const val PROBABLE_DOGS_IDS = "probable_dogs_ids"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DogedexTheme {
                DogDetailScreen(
                    finishActivity = { finish() }
                )
            }
        }
    }
}
