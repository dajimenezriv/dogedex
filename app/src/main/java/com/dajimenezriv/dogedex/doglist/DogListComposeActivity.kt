package com.dajimenezriv.dogedex.doglist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dajimenezriv.dogedex.dogdetail.DogDetailComposeActivity
import com.dajimenezriv.dogedex.dogdetail.ui.theme.DogedexTheme
import com.dajimenezriv.dogedex.models.Dog

class DogListComposeActivity : ComponentActivity() {
    private val viewModel: DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DogedexTheme {
                DogListScreen(
                    dogList = viewModel.dogs.value,
                    status = viewModel.status.value,
                    onNavigationIconClick = { finish() },
                    // because both functions accept the same number of params
                    // we can simplify it like this
                    onDogClicked = ::openDogDetailActivity,
                    onDialogDismiss = { viewModel.resetAPIResponseStatus()}
                )
            }
        }
    }

    private fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog)
        startActivity(intent)
    }
}