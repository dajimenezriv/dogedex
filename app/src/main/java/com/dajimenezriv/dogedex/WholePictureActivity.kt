package com.dajimenezriv.dogedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.load
import com.dajimenezriv.dogedex.databinding.ActivityWholePictureBinding
import java.io.File

class WholePictureActivity : AppCompatActivity() {
    companion object {
        const val PICTURE_URI_KEY = "picture_uri"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWholePictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pictureUri = intent.extras?.getString(PICTURE_URI_KEY)

        if (pictureUri.isNullOrEmpty()) {
            Toast.makeText(this, "Error showing image, no picture uri", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.wholePicture.load(pictureUri)
    }
}