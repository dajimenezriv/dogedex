package com.dajimenezriv.dogedex.dogdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import coil.load
import com.dajimenezriv.dogedex.models.Dog
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.databinding.ActivityDogDetailBinding

class DogDetailActivity : AppCompatActivity() {
    companion object {
        const val DOG_KEY = "dog"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }

    private val viewModel: DogDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
        val isRecognition = intent?.extras?.getBoolean(IS_RECOGNITION_KEY, false) ?: false

        if (dog == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish() // finish Activity
            return // and finish the code here
        }

        binding.dogIndex.text = getString(R.string.dog_index_format, dog.index)
        binding.lifeExpectancy.text = getString(R.string.dog_life_expectancy_format, dog.lifeExpectancy)
        binding.dog = dog
        binding.dogImage.load(dog.imageUrl)
        binding.closeButton.setOnClickListener {
            // we want to add the dog only if we have reach this activity by the take picture in ml
            if (isRecognition) viewModel.addDogToUser(dog.id)
            else finish()
        }

        viewModel.status.observe(this) { status ->
            when (status) {
                is APIResponseStatus.Error -> {
                    binding.loadingWheel.visibility = View.GONE
                    Toast.makeText(this, status.messageId, Toast.LENGTH_SHORT).show()
                }
                is APIResponseStatus.Loading -> {
                    // show progress bar
                    binding.loadingWheel.visibility = View.VISIBLE
                }
                is APIResponseStatus.Success -> {
                    // hide progress bar
                    binding.loadingWheel.visibility = View.GONE
                    finish()
                }
            }
        }
    }
}
