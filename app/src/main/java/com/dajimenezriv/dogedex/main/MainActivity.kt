package com.dajimenezriv.dogedex.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dajimenezriv.dogedex.LABELS_PATH
import com.dajimenezriv.dogedex.MODEL_PATH
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.WholePictureActivity
import com.dajimenezriv.dogedex.WholePictureActivity.Companion.PICTURE_URI_KEY
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.api.APIServiceInterceptor
import com.dajimenezriv.dogedex.auth.LoginComposeActivity
import com.dajimenezriv.dogedex.databinding.ActivityMainBinding
import com.dajimenezriv.dogedex.dogdetail.DogDetailComposeActivity.Companion.DOG_KEY
import com.dajimenezriv.dogedex.dogdetail.DogDetailComposeActivity.Companion.IS_RECOGNITION_KEY
import com.dajimenezriv.dogedex.dogdetail.DogDetailComposeActivity
import com.dajimenezriv.dogedex.dogdetail.DogDetailComposeActivity.Companion.PROBABLE_DOGS_IDS
import com.dajimenezriv.dogedex.doglist.DogListComposeActivity
import com.dajimenezriv.dogedex.models.User
import com.dajimenezriv.dogedex.settings.SettingsActivity
import com.dajimenezriv.dogedex.testutils.EspressoIdlingResource
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.support.common.FileUtil
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private var isCameraReady = false
    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // permission is granted
                setupCamera()
            } else {
                // explain to the user that the feature is unavailable
                Toast.makeText(
                    this,
                    getString(R.string.error_camera_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = User.getLoggedInUser(this)
        if (user == null) {
            startActivity(Intent(this, LoginComposeActivity::class.java))
            finish()
            return
        }

        APIServiceInterceptor.setSessionToken(user.authenticationToken)

        binding.dogListFab.setOnClickListener {
            startActivity(Intent(this, DogListComposeActivity::class.java))
        }

        binding.settingsFab.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
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
                }
            }
        }

        viewModel.dog.observe(this) { dog ->
            if (dog != null) {
                val intent = Intent(this, DogDetailComposeActivity::class.java)
                intent.putExtra(DOG_KEY, dog)
                intent.putExtra(PROBABLE_DOGS_IDS, ArrayList<String>(viewModel.probableDogIds))
                intent.putExtra(IS_RECOGNITION_KEY, true)
                startActivity(intent)
            }
        }

        viewModel.dogRecognition.observe(this) { dogRecognition ->
            // enable camera button
            if (dogRecognition.confidence > 70) {
                binding.takePhotoFab.alpha = 1f
                binding.takePhotoFab.setOnClickListener {
                    viewModel.getDogByMlId(dogRecognition.id)
                }
            } else {
                binding.takePhotoFab.alpha = 0.2f
                binding.takePhotoFab.setOnClickListener(null)
            }
        }

        requestCameraPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutor.isInitialized) cameraExecutor.shutdown()
    }

    private fun requestCameraPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // we can use the api that requires the permission
                    setupCamera()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                -> {
                    // explain why the user needs the permission
                    // this appears in case that the user has cancel the permission before
                    AlertDialog.Builder(this)
                        .setTitle("Camera Permission")
                        .setMessage("Accept Camera Permission to track dogs.")
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                        .setNegativeButton(android.R.string.cancel) { _, _ -> }
                        .show()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        } else {
            // open camera, we don't need the permission
            setupCamera()
        }
    }

    private fun setupCamera() {
        // we will wait until the cameraPreview is initialized
        // otherwise, it will throw a null pointer exception when trying to access to display
        binding.cameraPreview.post {
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()
            cameraExecutor = Executors.newSingleThreadExecutor()
            startCamera()
            isCameraReady = true
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        // for testing
        EspressoIdlingResource.increment()
        cameraProviderFuture.addListener({
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

            // machine learning
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                EspressoIdlingResource.decrement()
                viewModel.recognizeImage(imageProxy)
                // we can't close the imageProxy here because it's running inside a coroutine
                // imageProxy.close()
            }

            // camera to lifecycle
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePicture() {
        if (!isCameraReady) return

        val outputFileOptions =
            ImageCapture.OutputFileOptions.Builder(getOutputPictureFile()).build()
        imageCapture.takePicture(
            // a cameraExecutor is just a thread to execute the camera there
            outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error taking picture: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // val uri = outputFileResults.savedUri
                    // openPicture(uri.toString())
                }
            }
        )
    }

    private fun getOutputPictureFile(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name) + ".jpg").apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private fun openPicture(pictureUri: String) {
        val intent = Intent(this, WholePictureActivity::class.java)
        intent.putExtra(PICTURE_URI_KEY, pictureUri)
        startActivity(intent)
    }
}